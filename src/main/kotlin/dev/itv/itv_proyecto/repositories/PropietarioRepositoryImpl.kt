package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection

class PropietarioRepositoryImpl : ModelsRepository<Propietario, String, PropietarioErrors>, KoinComponent {
    private val logger = KotlinLogging.logger {}
    val manager : DatabaseManager by inject()
    var database = manager.bd

    /**
     *  Funcion que busca un Propietario en la base de datos en funcion del dni del propietario
     *
     *  @param id Dni del Propietario que buscar
     *  @return Propietario con el dni deseado o los posibles errores con Result
     *  @see loadAll
     */
    override fun findById(id: String): Result<Propietario, PropietarioErrors> {
        logger.debug { " PropietarioRepositoryImpl -- FindByID ($id) " }
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.dni == id }?.let {
            return Ok(it)
        }
        return Err(PropietarioErrors.PropietarioNotFoundError("No existe un propietario con la id $id"))
    }

    /**
     * Función que hace un Select a la Base de datos de toda la tabla Propietarios y los devuelve como lista
     *
     * @return Lista de Propietarios de la Tabla tPropietarios o los Posibles errores de la consulta con Result
     * @see Utils.selectAllFromTable
     */
    override fun loadAll(): Result<List<Propietario>, PropietarioErrors> {
        return selectAllPropietarios(database)
    }

    private fun selectAllPropietarios(database: Connection): Result<List<Propietario>, PropietarioErrors.PropietarioQueryErrors> {
        logger.debug { " PropietarioRepositoryImpl -- selectAllPropietarios () " }
        val propietarios = mutableListOf<Propietario>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tPropietario").let{ resultSet ->
            while (resultSet.next()) {
                propietarios.add(
                    Propietario(
                        dni = resultSet.getString("cDNI"),
                        nombre = resultSet.getString("cNombre"),
                        apellidos = resultSet.getString("cApellidos"),
                        telefono = resultSet.getInt("nTelefono"),
                        emailPropietario = resultSet.getString("cCorreo_electronico")
                    )
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(propietarios)
    }

    /**
     * Función que hace un insert en la Base de Datos siempre que no exista ya.
     *
     * @param propietario Propietario que vamos a guardar en la base de datos
     * @return Propietario tras guardarse en la base de datos o los posibles errores con Result
     */
    fun savePropietario(propietario: Propietario) : Result<Propietario, PropietarioErrors> {
        logger.debug { "PropietarioRepositoryImpl ----" }
        findById(propietario.dni).onSuccess {
            return Err(PropietarioErrors.PropietarioFoundError("Ya existe una persona con este dni : ${propietario.dni}"))
        }
        findByEmail(propietario.emailPropietario).onSuccess {
            return Err(PropietarioErrors.PropietarioFoundError("Ya existe una persona con este email : ${propietario.emailPropietario}"))
        }
        val sql = """
            INSERT INTO tPropietario VALUES (?, ?, ?, ?, ?) 
        """.trimIndent()
        try {
            database.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setString(1,propietario.dni)
                preparedStatement.setString(2, propietario.nombre)
                preparedStatement.setString(3, propietario.apellidos)
                preparedStatement.setInt(4, propietario.telefono)
                preparedStatement.setString(5, propietario.emailPropietario)
                preparedStatement.executeUpdate()
                preparedStatement.close()
            }
        }catch (e : Exception) {
            logger.error { e }
            return Err(PropietarioErrors.PropietarioQueryErrors("No se ha podido guardar el Propietario con DNI ${propietario.dni}"))
        }

        return Ok(propietario)
    }

    /**
     * Función que encuentra un propietario por su email, debido a que el campo email es unicode
     *
     * @param email Email sobre el que buscaremos el propietario
     * @return Propietario encontrado o los posibles errores.
     * @see loadAll
     */
    private fun findByEmail(email: String): Result<Propietario, PropietarioErrors> {
        logger.debug { " PropietarioRepositoryImpl -- FindByEmail ($email) " }
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.emailPropietario == email }?.let {
            return Ok(it)
        }
        return Err(PropietarioErrors.PropietarioNotFoundError("No existe un propietario con el email $email"))
    }

}