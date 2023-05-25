package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection

class PropietarioRepositoryImpl : ModelsRepository<Propietario, String, PropietarioErrors>, KoinComponent {

    val manager : DatabaseManager by inject()
    val database = manager.bd

    override fun findById(id: String): Result<Propietario, PropietarioErrors> {
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.dni == id }?.let {
            return Ok(it)
        }
        return Err(PropietarioErrors.PropietarioNotFoundError("No existe un propietario con la id $id"))
    }

    override fun loadAll(): Result<List<Propietario>, PropietarioErrors> {
        return selectAllPropietarios(database)
    }

    override fun deleteAll(): Boolean {
        return Utils.deleteFromTable(database, "tPropietario")
    }

    private fun selectAllPropietarios(database: Connection): Result<List<Propietario>, PropietarioErrors.PropietarioQueryErrors> {
        Utils.logger.debug { " Utils : selectAllPropietarios () " }
        val propietarios = mutableListOf<Propietario>()
        val statement = database.createStatement()
        Utils.selectAllFromTable(database, "tPropietario").onFailure {
            return Err(PropietarioErrors.PropietarioQueryErrors(it.message!!))
        }.onSuccess { resultSet ->
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
}