package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.TrabajadorErrors
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.Utils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.test.KoinTest
import java.sql.Connection

class TrabajadorRepositoryImpl : ModelsRepository<Trabajador, Long, TrabajadorErrors> , KoinComponent {

    val manager : DatabaseManager by inject()
    val database = manager.bd

    override fun findById(id: Long): Result<Trabajador, TrabajadorErrors> {
        loadAll().onFailure {
            return Err(it)
        }.component1()!!.find { it.idTrabajador == id }?.let {
            return Ok(it)
        }
        return Err(TrabajadorErrors.TrabajadorNotFoundError("No existe un trabajador con la id $id"))
    }

    override fun loadAll(): Result<List<Trabajador>, TrabajadorErrors> {
        return selectAllTrabajadores(database)
    }

    private fun selectAllTrabajadores(database: Connection): Result<List<Trabajador>, TrabajadorErrors> {
        Utils.logger.debug { " Utils : selectAllTrabajadores () " }
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
        return Ok(trabajadores)
    }

}