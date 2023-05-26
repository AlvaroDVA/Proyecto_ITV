package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VehiculoRepositoryImpl : ModelsRepository<Vehiculo, String, VehiculosErrors>, KoinComponent{

    private val logger = KotlinLogging.logger {  }
    val manager : DatabaseManager by inject()
    var database = manager.bd

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
        return selectAllVehiculos()
    }

    private fun selectAllVehiculos(): Result<List<Vehiculo>, VehiculosErrors> {
        Utils.logger.debug { " Utils : selectAllVehiculos () " }
        val vehiculos = mutableListOf<Vehiculo>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tVehiculo").let { resultSet ->
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