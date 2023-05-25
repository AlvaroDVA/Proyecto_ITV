package dev.itv.itv_proyecto.utils

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.DatabaseErrors
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Vehiculo
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

object Utils {
    val logger = KotlinLogging.logger {  }

    fun selectAllFromTable (conexion : Connection, nombreTabla : String) : Result<ResultSet, DatabaseErrors>{
        logger.debug { " Utils : Select * FROM $nombreTabla " }
        val statement : Statement = conexion.createStatement()
        val query = "SELECT * FROM $nombreTabla"
        statement.executeQuery(query)?.let {
            statement.close()
            return Ok(it)
        }?:let {
            statement.close()
            return Err(DatabaseErrors.ConsultaDbError("Select All From $nombreTabla ha fallado"))
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

    fun deleteFromTable(database: Connection, tabla: String) : Boolean{
        logger.debug { " Utils : DeleteFromTable ($tabla) " }
        val sql = """
            DELETE FROM ?;
        """.trimIndent()
        database.prepareStatement(sql).use {
            it.setString(1, tabla)
            it.executeUpdate()
            it.close()
            return true
        }
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





}