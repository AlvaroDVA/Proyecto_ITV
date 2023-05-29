package dev.itv.itv_proyecto.utils

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Vehiculo
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

object Utils {
    val logger = KotlinLogging.logger {  }

    /**
     * Funcion que seleccionara todas las filas de una base de datos en función del nombre de la tabla
     *
     * @param conexion Conexion con la base de datos sobre la que se hará el select
     * @param nombreTabla Nombre de la tabla sobre la que se hará el select
     * @return ResultSet con el resultado de la consulta
     */
    fun selectAllFromTable (conexion : Connection, nombreTabla : String) : ResultSet{
        logger.debug { " Utils : Select * FROM $nombreTabla " }
        val statement : Statement = conexion.createStatement()
        val query = "SELECT * FROM $nombreTabla"
        statement.executeQuery(query).run {
            statement.close()
            return this
        }
    }

    /**
     * Funcion que genera un Vehículo en función de una consulta y el propietario del vehículo
     *
     * @param resultSet Resultado de la consulta
     * @param propietario Propietario de al que se le asignara un vehículo
     * @return Vehículo formado con los datos del resultSet y el propietario ya incluido en él
     * @see generarMotor
     * @see generarTipoVehiculo
     *
     */
    fun generarVehiculo(resultSet: ResultSet, propietario : Propietario): Vehiculo = Vehiculo(
        matricula = resultSet.getString(1),
        marca = resultSet.getString(2),
        modelo = resultSet.getString(3),
        fechaMatricula = resultSet.getDate(4).toLocalDate(),
        fechaUltimaRevision = resultSet.getDate(5).toLocalDate(),
        tipoMotor = generarMotor(resultSet.getString(6)),
        tipoVehiculo = generarTipoVehiculo(resultSet.getString(7)),
        propietario = propietario

    ).also {
        logger.debug { " Vehiculo Generado $it " }
    }

    /**
     * Función que nos generará un Enum de tipo TipoMotor según un String. En la base de datos no pueden entrar valores que no son motores,
     * no llegará nada que no sea un Enum
     *
     * @param motor String sobre el que se seleccionara el TipoMotor
     * @return Un Enum TipoMotor con el valor equivalente al parametro al pasarlo a String
     */
    private fun generarMotor(motor: String): TipoMotor {
        logger.debug { " Utils : GenerarMotor() " }
        return when (motor) {
            TipoMotor.DIESEL.toString() -> TipoMotor.DIESEL
            TipoMotor.ELECTRICO.toString() -> TipoMotor.ELECTRICO
            TipoMotor.HIBRIDO.toString() -> TipoMotor.HIBRIDO
            else -> TipoMotor.GASOLINA
        }
    }
    /**
     * Función que nos generará un Enum de tipo TipoVehiculo según un String. En la base de datos no pueden entrar valores
     * que no son tipos de vehiculos, no llegará nada que no sea un Enum
     *
     * @param tipo String sobre el que se seleccionara el TipoVehiculo
     * @return Un Enum TipoVehiculo con el valor equivalente al parametro al pasarlo a String
     */
    private fun generarTipoVehiculo(tipo: String) : TipoVehiculo {
        logger.debug { " Utils : GenerarTipoVehiculo() " }
        return when (tipo) {
            TipoVehiculo.CAMION.toString() -> TipoVehiculo.CAMION
            TipoVehiculo.FURGONETA.toString() -> TipoVehiculo.FURGONETA
            TipoVehiculo.TURISMO.toString()-> TipoVehiculo.TURISMO
            else -> TipoVehiculo.MOTOCICLETA
        }
    }

    /**
     * Función que nos busca en la base de datos un Propietario por el Dni.
     */
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
            }
        }
        return Err(PropietarioErrors.PropietarioNotFoundError("No se ha encontrado un propietario en la BD con el dni $dni"))
    }

    /**
     * Función que nos generará un Enum de tipo Especialidad según un String. En la base de datos no pueden entrar valores
     * que no son Especialidades, no llegará nada que no sea un Enum
     *
     * @param especialidad String sobre el que se seleccionara la Especialidad
     * @return Un Enum Especialidad con el valor equivalente al parametro al pasarlo a String
     */
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