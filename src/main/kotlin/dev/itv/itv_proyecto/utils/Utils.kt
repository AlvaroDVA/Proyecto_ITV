package dev.itv.itv_proyecto.utils

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.Vehiculo
import javafx.scene.chart.PieChart.Data
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDate

object Utils {
    val logger = KotlinLogging.logger {  }

    fun selectAllFromTable (conexion : Connection, nombreTabla : String) : ResultSet{
        logger.debug { " Utils : Select * FROM $nombreTabla " }
        val statement : Statement = conexion.createStatement()
        val query = "SELECT * FROM $nombreTabla"
        statement.executeQuery(query).run {
            statement.close()
            return this
        }
    }

    fun generarVehiculo(resultSet: ResultSet, propietario : Propietario): Vehiculo = Vehiculo(
        matricula = resultSet.getString(1),
        marca = resultSet.getString(2),
        modelo = resultSet.getString(3),
        fechaMatricula = resultSet.getDate(4).toLocalDate(),
        fechaUltimaRevision = resultSet.getDate(5).toLocalDate(),
        tipoMotor = generarMotor(resultSet),
        tipoVehiculo = generarTipoVehiculo(resultSet),
        propietario = propietario

    ).also {
        logger.debug { " Vehiculo Generado $it " }
    }

    private fun generarMotor(resultSet: ResultSet): TipoMotor {
        logger.debug { " Utils : GenerarMotor() " }
        return when (resultSet.getString(6)) {
            TipoMotor.DIESEL.toString() -> TipoMotor.DIESEL
            TipoMotor.ELECTRICO.toString() -> TipoMotor.ELECTRICO
            TipoMotor.HIBRIDO.toString() -> TipoMotor.ELECTRICO
            else -> TipoMotor.GASOLINA
        }
    }

    private fun generarTipoVehiculo(resultSet: ResultSet) : TipoVehiculo {
        logger.debug { " Utils : GenerarTipoVehiculo() " }
        return when (resultSet.getString(7)) {
            TipoVehiculo.CAMION.toString() -> TipoVehiculo.CAMION
            TipoVehiculo.FORGONETA.toString() -> TipoVehiculo.FORGONETA
            TipoVehiculo.TURISMO.toString()-> TipoVehiculo.TURISMO
            else -> TipoVehiculo.MOTOCICLETA
        }
    }

    fun findPropietarioByDni (conexion: Connection, dni : String) : Result<Propietario, PropietarioErrors> {
        logger.debug { " Utils : FindPropietarioByDni() " }
        val propietario : Propietario
        val sql = "SELECT * FROM tPropietario WHERE cDni = ?"
        conexion.prepareStatement(sql).use { statement ->
            statement.setString(1,dni)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    propietario = Propietario(
                        dni = resultSet.getString(1),
                        nombre = resultSet.getString(2),
                        apellidos = resultSet.getString(3),
                        telefono = resultSet.getInt(4),
                        emailPropietario = resultSet.getString(5)
                    )
                    resultSet.close()
                    statement.close()
                    return Ok(propietario)
                }
                resultSet
            }
            statement.close()
        }
        return Err(PropietarioErrors.PropietarioNotFoundError("No se ha encontrado un propietario en la BD con el dni $dni"))
    }

    fun sacarEspecialidad(especialidad: String?): Especialidad = when (especialidad) {
        Especialidad.MOTOR.toString() -> Especialidad.MOTOR
        Especialidad.ELECTRICIDAD.toString() -> Especialidad.ELECTRICIDAD
        Especialidad.INTERIOR.toString() -> Especialidad.INTERIOR
        Especialidad.ADMINISTRACION.toString() -> Especialidad.ADMINISTRACION
        else -> Especialidad.MECANICA
    }.also {
        logger.debug { " Utils : SacarEspecialidad ($especialidad) " }
    }

    fun trabajadorByEmail(conexion: Connection, email: String) : Trabajador? {

        val sql = """
            SELECT * FROM tTrabajador WHERE cEmail = ?;
        """.trimIndent()
        var trabajador : Trabajador?

        conexion.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, email)
            preparedStatement.executeQuery()?.let { resultSet ->
                if (resultSet.next()) {
                    return Trabajador(
                        idTrabajador = resultSet.getLong("nId_Trabajador"),
                        nombreTrabajador = resultSet.getString("cNombre"),
                        telefonoTrabajador = resultSet.getInt("nTelefono"),
                        email = resultSet.getString("cEmail"),
                        username = resultSet.getString("cUsuario"),
                        password = resultSet.getString("cContrasena"),
                        fechaContratacion = resultSet.getDate("dFecha_contratacion").toLocalDate(),
                        especialidad = sacarEspecialidad(resultSet.getString("cEspecialidad")),
                        isResponsable = resultSet.getInt("nEs_Jefe") == 1
                    )
                }
            }?:let {
                return null
            }
        }
        return null
    }



}