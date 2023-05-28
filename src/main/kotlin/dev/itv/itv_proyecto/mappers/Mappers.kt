package dev.itv.itv_proyecto.mappers

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.utils.Utils
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.sql.Connection
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }
class Mappers : KoinComponent{
    fun toDto (informe : Informe) : InformeDto {
        logger.info { " Mapeando Informe a Dto ---- $this " }
        return InformeDto(
            idInforme = informe.idInforme.toString(),
            apto = if (informe.apto == true) "true" else "false",
            frenado = informe.frenado.toString(),
            contaminacion = informe.contaminacion.toString(),
            interior = if (informe.interior == true) "true" else "false",
            luces = if (informe.luces == true) "true" else "false",
            idTrabajador = informe.trabajador.idTrabajador.toString(),
            nombreTrabajador = informe.trabajador.nombreTrabajador,
            email = informe.trabajador.email,
            matricula = informe.vehiculo.matricula,
            marca = informe.vehiculo.marca,
            modelo = informe.vehiculo.modelo,
            fechaMatricula = informe.vehiculo.fechaMatricula.toString(),
            fechaUltimaRevision = informe.vehiculo.fechaUltimaRevision.toString(),
            tipoMotor = informe.vehiculo.tipoMotor.toString(),
            tipoVehiculo = informe.vehiculo.tipoVehiculo.toString(),
            dni = informe.propietario.dni,
            nombre = informe.propietario.nombre,
            apellidos = informe.propietario.apellidos,
            telefono = informe.propietario.telefono.toString(),
            emailPropietario = informe.propietario.emailPropietario,
            horaCita = informe.horaCita,
            fechaCita = informe.fechaCita.toString()
        )
    }



    fun informeDtoToInforme (conexion : Connection, informe : InformeDto) : Informe? {
        logger.info { " Mapeado DtoInforme a Informe " }
        return Informe(
            idInforme = informe.idInforme.toLongOrNull() ?: -1L,
            apto = informe.apto == "1" || informe.apto == "true",
            frenado = informe.frenado.toDoubleOrNull(),
            contaminacion = informe.contaminacion.toDoubleOrNull(),
            interior = informe.interior == "1" || informe.interior == "true",
            luces = informe.luces == "1" || informe.luces == "true",
            trabajador = trabajadorByEmail(conexion, informe.email)
                ?:let {
                    return null
            },
            vehiculo = Vehiculo (
                matricula = informe.matricula.uppercase(),
                marca = informe.marca,
                modelo = informe.modelo,
                fechaMatricula = LocalDate.parse(informe.fechaMatricula),
                fechaUltimaRevision = LocalDate.parse(informe.fechaUltimaRevision),
                tipoMotor = sacaMotor(informe.tipoMotor),
                tipoVehiculo = sacarTipoVehiculo(informe.tipoVehiculo),
                propietario = Propietario(
                    dni = informe.dni,
                    apellidos = informe.apellidos,
                    emailPropietario = informe.emailPropietario,
                    nombre = informe.nombre,
                    telefono = informe.telefono.toInt()
                )
            ),
            propietario = Propietario(
                dni = informe.dni.uppercase(),
                apellidos = informe.apellidos,
                emailPropietario = informe.emailPropietario,
                nombre = informe.nombre,
                telefono = informe.telefono.toInt()
            ),
            fechaCita = LocalDate.parse(informe.fechaCita),
            horaCita = informe.horaCita
        )
    }

    private fun sacaMotor(tipoMotor: String): TipoMotor {
        return when (tipoMotor) {
            TipoMotor.GASOLINA.toString() -> TipoMotor.GASOLINA
            TipoMotor.HIBRIDO.toString() -> TipoMotor.HIBRIDO
            TipoMotor.ELECTRICO.toString() -> TipoMotor.ELECTRICO
            else -> TipoMotor.DIESEL
        }
    }

    private fun sacarTipoVehiculo(tipoVehiculo : String) : TipoVehiculo {
        return when (tipoVehiculo) {
            TipoVehiculo.TURISMO.toString() -> TipoVehiculo.TURISMO
            TipoVehiculo.MOTOCICLETA.toString() -> TipoVehiculo.MOTOCICLETA
            TipoVehiculo.FURGONETA.toString() -> TipoVehiculo.FURGONETA
            else -> TipoVehiculo.CAMION
        }
    }

    fun trabajadorByEmail(conexion: Connection, email: String) : Trabajador? {

        val sql = """
            SELECT * FROM tTrabajador WHERE cEmail = ?;
        """.trimIndent()

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
                        especialidad = Utils.sacarEspecialidad(resultSet.getString("cEspecialidad")),
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