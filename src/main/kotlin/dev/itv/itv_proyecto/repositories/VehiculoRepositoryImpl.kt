package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Date

class VehiculoRepositoryImpl : VehiculoRepository, KoinComponent{

    private val logger = KotlinLogging.logger {  }
    private val databaseManager : DatabaseManager by inject()
    private val database = databaseManager.bd

    /**
     * Función que pide una cadena de texto por parámetros y devuelve una lista de Vehículos cuya matrícula contenga esta cadena
     *
     * @author Álvaro Del Val Arce
     * @return Lista filtrada por matricula o posibles errores gracias a Result
     * @param  matricula La cadena de texto que servirá para filtrar los Vehículos
     * @see loadAll
    */
    override fun filterByMatricula(matricula: String): Result<List<Vehiculo>, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- FilterByMatricula ($matricula) " }
        loadAll().onSuccess {vehiculos ->
            return Ok(vehiculos.filter { it.matricula.contains(matricula) })
        }.component2().let {
            return Err(it!!)
        }
    }

    /**
     * Función que pide por parametro un Enum de tipo TipoVehiculo para poder filtrar todos los Vehiculos según el tipo de vehículo que es.
     *
     * @author Álvaro Del Val Arce
     * @return Lista filtrada por tipo de vehículo posibles errores gracias a Result
     * @param tipo Tipo de Vehículo por el que se filtrara los vehículos
     * @see loadAll
     */
    override fun filterByTipo(tipo: TipoVehiculo): Result<List<Vehiculo>, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- FilterByTipo ($tipo) " }
        loadAll().onSuccess {vehiculos ->
            return Ok(vehiculos.filter { it.tipoVehiculo == tipo })
        }.component2().let {
            return Err(it!!)
        }
    }

    /**
     * Función que guarda un Vehículo cuyos campos vendrán ya validados en el controlador en una Base de Datos camp
     *
     * @author Álvaro Del Val Arce
     * @return Vehículo tras guardarse en la Base de datos o los posibles errores gracias a Result
     * @param item Vehículo que guardaremos en la Base de Datos
     * @see findById
     */
    override fun save(item: Vehiculo): Result<Vehiculo, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImp -- Save ($item)" }
        findById(item.matricula).onSuccess {
            return Err(VehiculosErrors.VehiculoConsultaError("Ya existe un vehiculo con la matricula ${item.matricula}"))
        }

        val sql = """
            INSERT INTO tVehiculo VALUES (?,?,?,?,?,?,?,?)
        """.trimIndent()

        database.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1,item.matricula)
            preparedStatement.setString(2,item.marca)
            preparedStatement.setString(3,item.modelo)
            preparedStatement.setDate(4, Date.valueOf(item.fechaMatricula))
            preparedStatement.setDate(5, Date.valueOf(item.fechaUltimaRevision))
            preparedStatement.setString(6, item.tipoMotor.toString())
            preparedStatement.setString(7, item.tipoVehiculo.toString())
            preparedStatement.setString(8, item.propietario.dni)

            preparedStatement.executeUpdate()

            preparedStatement.close()
        }

        findById(item.matricula).onSuccess {
            return Ok(it)
        }
        return Err(VehiculosErrors.VehiculoNotFoundError("No se ha guardado el vehículo con matricula ${item.matricula}"))
    }


    /**
     * Función que elimina un Vehículo de la base de datos según una matrícula si existiese.
     *
     * @author Álvaro Del Val Arce
     * @return El vehículo eliminado o los posibles errores gracias a Result
     * @param id Matrícula que buscaremos en la Base de datos para borrarla
     * @see findById
     *
     */
    override fun deleteById(id: String): Result<Vehiculo, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- DeleteById ($id)" }
        val old = findById(id).onFailure {
            return Err(it)
        }.component1()!!
        val sql = """
            DELETE FROM tVehiculo WHERE cMatricula = ?
        """.trimIndent()
        database.prepareStatement(sql).use {preparedStatement ->
            preparedStatement.setString(1,id)
            preparedStatement.executeUpdate()

            preparedStatement.close()
        }
        findById(id).onSuccess {
            return Err(VehiculosErrors.VehiculoConsultaError("No se ha borrado el vehiculo con matricula $id"))
        }
        return Ok(old)
    }

    /**
     * Función que devuelve el resultado de un select en la base de datos buscando una matrícula de un vehículo
     *
     * @author Álvaro Del Val Arcen
     * @return Vehículo con la matrícula a buscar o los posibles errores gracias al resultado de un select
     * @param id Matrícula con la que se hará el Select en la Base de datos
     * @see loadAll
     */

    override fun findById(id: String): Result<Vehiculo, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- FilterById ($id) " }
        loadAll().onSuccess { vehiculos ->
            vehiculos.find{ it.matricula == id }?.let {
                return Ok(it)
            } ?:let {
                return Err(VehiculosErrors.VehiculoNotFoundError("No existe un vehículo con la matricula $id"))
            }
        }.let {
            return Err(it.component2()!!)
        }
    }

    /**
     *
     */
    override fun updateById(id: String, item: Vehiculo): Result<Vehiculo, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- updateById ($id , $item)" }
        val old = findById(id).onFailure {
            return Err(it)
        }.component1()

        val sql = """
            UPDATE tVehiculo
             SET cMarca = ?,
             cModelo = ?,
             dFecha_matriculacion = ?,
             dFecha_ultima_revision = ?,
             tipo_motor = ?,
             tipo_vehiculo = ?,
             cDNI_Propietario = ?
              WHERE cMatricula = ?
        """.trimIndent()
        database.prepareStatement(sql).use {statement ->

            statement.setString(1, item.marca)
            statement.setString(2, item.modelo)
            statement.setDate(3, Date.valueOf(item.fechaMatricula))
            statement.setDate(4, Date.valueOf(item.fechaUltimaRevision))
            statement.setString(5, item.tipoMotor.toString())
            statement.setString(6, item.tipoVehiculo.toString())
            statement.setString(7, item.propietario.dni)
            statement.setString(8, id)
            statement.executeUpdate()
            statement.close()

            findById(id).onFailure {
                return Err(it)
            }.onSuccess{
                if (old == it) {
                    return Err(VehiculosErrors.VehiculoNotFoundError("No se ha actualizado el vehiculo con id $id"))
                }
                return Ok(it)
            }

        }


        return Err(VehiculosErrors.VehiculoNotFoundError("No se ha actualizado el vehiculo con id $id"))
    }

    override fun loadAll(): Result<List<Vehiculo>, VehiculosErrors> {
        val vehiculos = mutableListOf<Vehiculo>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tVehiculo").onFailure {
            return Err(VehiculosErrors.VehiculoConsultaError(it.message!!))
        }.onSuccess { resultSet ->
            while (resultSet.next()) {
                val propietario = Utils.findPropietarioByDni(database, resultSet.getString(8)).onFailure {
                    return Err(VehiculosErrors.VehiculoNotFoundError(it.message!!))
                }

                vehiculos.add(
                    Utils.generarVehiculo(resultSet, propietario.component1()!!)
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(vehiculos)
    }

    override fun deleteAll(): Boolean{
        val sql = """
            DELETE FROM tVehiculo;
        """.trimIndent()
        database.createStatement().use {
            it.executeUpdate(sql)
            return true
        }
    }

}