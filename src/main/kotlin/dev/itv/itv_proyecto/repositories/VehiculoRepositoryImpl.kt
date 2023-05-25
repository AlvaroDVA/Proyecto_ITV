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
import java.sql.Connection

class VehiculoRepositoryImpl : ModelsRepository<Vehiculo, String, VehiculosErrors>, KoinComponent{

    private val logger = KotlinLogging.logger {  }
    private val databaseManager : DatabaseManager by inject()
    private val database = databaseManager.bd

    /**
     * Función que pide una cadena de texto por parámetros y devuelve una lista de Vehículos cuya matrícula contenga esta cadena
     *
     * @return Lista filtrada por matricula o posibles errores gracias a Result
     * @param  matricula La cadena de texto que servirá para filtrar los Vehículos
     * @see loadAll
    */
    fun filterByMatricula(matricula: String): Result<List<Vehiculo>, VehiculosErrors> {
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
     * @return Lista filtrada por tipo de vehículo posibles errores gracias a Result
     * @param tipo Tipo de Vehículo por el que se filtrara los vehículos
     * @see loadAll
     */
    fun filterByTipo(tipo: TipoVehiculo): Result<List<Vehiculo>, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- FilterByTipo ($tipo) " }
        loadAll().onSuccess {vehiculos ->
            return Ok(vehiculos.filter { it.tipoVehiculo == tipo })
        }.component2().let {
            return Err(it!!)
        }
    }


    /**
     * Función que devuelve el resultado de un select en la base de datos buscando una matrícula de un vehículo
     *
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

    override fun loadAll(): Result<List<Vehiculo>, VehiculosErrors> {
        logger.debug { " VehiculoRepositoryImpl -- LoadAll() " }
        return selectAllVehiculos(database)
    }

    override fun deleteAll(): Boolean{
        logger.debug { " VehiculoRepositoryImpl -- deleteAll() " }
        return Utils.deleteFromTable(database, "tVehiculo")
    }

    private fun selectAllVehiculos(database: Connection): Result<List<Vehiculo>, VehiculosErrors> {
        Utils.logger.debug { " Utils : selectAllVehiculos () " }
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

}