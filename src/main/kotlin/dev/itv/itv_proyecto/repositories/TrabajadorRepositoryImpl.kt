package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import dev.itv.itv_proyecto.errors.TrabajadorErrors
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TrabajadorRepositoryImpl : ModelsRepository<Trabajador, Long, TrabajadorErrors> , KoinComponent {

    val manager : DatabaseManager by inject()
    var database = manager.bd
    private val logger = KotlinLogging.logger {  }

    /**
     * Funcion para encontrar un Trabajador por su Id
     *
     * @param id Id del Trabajador por el que buscaremos
     * @return Trabajador si se ha encontrado o los posibles errores con Result
     */
    override fun findById(id: Long): Result<Trabajador, TrabajadorErrors> {
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.idTrabajador == id }?.let {
            return Ok(it)
        }
        return Err(TrabajadorErrors.TrabajadorNotFoundError("No existe un trabajador con la id $id"))
    }

    /**
     * Función para cargar todos los trabajadores
     *
     * @return Lista de todos los trabajadores de la Base de datos o los posibles errores con Result
     * @see selectAllTrabajadores
     */
    override fun loadAll(): Result<List<Trabajador>, TrabajadorErrors> {
        return selectAllTrabajadores()
    }

    /**
     * Función para hacer un select de toda la tabla tTrabajador
     *
     * @return Lista de todos los trabajadores de la base de datos o los posibles errores con Result
     * @see Utils.selectAllFromTable
     */
    private fun selectAllTrabajadores(): Result<List<Trabajador>, TrabajadorErrors> {
        logger.debug { " Utils : selectAllTrabajadores () " }
        val trabajadores = mutableListOf<Trabajador>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tTrabajador").let { resultSet ->
            while (resultSet.next()) {
                trabajadores.add(
                    Trabajador(
                        idTrabajador = resultSet.getLong("nId_Trabajador"),
                        nombreTrabajador = resultSet.getString("cNombre"),
                        telefonoTrabajador = resultSet.getInt("nTelefono"),
                        email = resultSet.getString("cEmail"),
                        username = resultSet.getString("cUsuario"),
                        password = resultSet.getString("cContrasena"),
                        fechaContratacion = resultSet.getDate("dFecha_contratacion").toLocalDate(),
                        especialidad = Utils.sacarEspecialidad(resultSet.getString("cEspecialidad")),
                        isResponsable = resultSet.getInt("nEs_jefe") == 1
                    )
                )
            }
            resultSet.close()
            statement.close()

        }
        return Ok(trabajadores.toList())
    }

}