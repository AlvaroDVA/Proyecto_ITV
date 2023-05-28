package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Date

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

    private fun findPropietarioByDNI(dni: String): Result<Propietario, VehiculosErrors> {
        logger.debug { " PropietarioRepositoryImpl -- FindByID ($dni) " }
        val propietarios = mutableListOf<Propietario>()
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
        }
        propietarios.find { it.dni == dni }?.let {
            return Ok(it)
        }
        return Err(VehiculosErrors.VehiculoNotFoundError("No existe un propietario con la matricula $dni para este Vehículo"))
    }

    fun save(item : Vehiculo) : Result<Vehiculo, VehiculosErrors> {
        logger.debug { "VehiculoRepositoryImpl -- Save ($item)" }
        findById(item.matricula).onSuccess {
            logger.warn { "Ya existe un vehículo con la matricula ${item.matricula}" }
            return Err(VehiculosErrors.VehiculoFoundError("Ya existe un vehículo con la matricula ${item.matricula}"))
        }
        findPropietarioByDNI(item.propietario.dni).onFailure {
            logger.warn { "No existe un propietario con la matricula ${item.propietario.dni} para este Vehículo" }
            return Err(it)
        }
        val sql = """
            INSERT INTO tVehiculo VALUES (?,?,?,?,?,?,?,?)
        """.trimIndent()

        try {
            database.prepareStatement(sql).let { preparedStatement ->
                preparedStatement.setString(1,item.matricula)
                preparedStatement.setString(2,item.marca)
                preparedStatement.setString(3,item.modelo)
                preparedStatement.setDate(4,Date.valueOf(item.fechaMatricula))
                preparedStatement.setDate(5,Date.valueOf(item.fechaUltimaRevision))
                preparedStatement.setString(6, item.tipoMotor.toString())
                preparedStatement.setString(7, item.tipoVehiculo.toString())
                preparedStatement.setString(8, item.propietario.dni)
                preparedStatement.executeUpdate()
            }
        }catch (e : Exception) {
            logger.error(e.message)
            return Err(VehiculosErrors.VehiculoConsultaError("No se ha podido guardar el Vehiculo : $e"))
        }
        return Ok(item)
    }
}