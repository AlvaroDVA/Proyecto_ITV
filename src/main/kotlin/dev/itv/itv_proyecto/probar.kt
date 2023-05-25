package dev.itv.itv_proyecto

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import mu.KotlinLogging
import java.time.LocalDate

fun probar () {
    val logger = KotlinLogging.logger {  }

    val repositorioInformes = InformeRepositoryImpl()

    val informeNuevo = Informe(
        apto = true,
        contaminacion = 45.0,
        frenado = 5.00,
        luces = true,
        interior = false,
        horaCita = "15:30",
        fechaCita = LocalDate.parse("2021-12-12"),
        trabajador = Trabajador(
            idTrabajador = 3,
            nombreTrabajador = "Trabajador 3",
            telefonoTrabajador = 69988882,
            email = "email3@example.com",
            username = "usuario3",
            password = "contrasena3",
            fechaContratacion = LocalDate.parse("2022-03-01"),
            especialidad = Especialidad.MOTOR,
            isResponsable = false,
        ),
        vehiculo = Vehiculo(
            matricula = "6666HKY",
            marca = "Toyota",
            modelo = "Carolla",
            fechaMatricula = LocalDate.parse("2020-05-15"),
            fechaUltimaRevision = LocalDate.parse("2022-05-15"),
            tipoMotor = TipoMotor.ELECTRICO,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = Propietario(
                dni = "7654321C",
                nombre = "Lucas",
                apellidos = "Garcia",
                telefono = 551115555,
                emailPropietario = "Lucas3@example.com"
            )
        )

    )

    repositorioInformes.loadAll().onFailure {
        logger.error { it }
    }.onSuccess {
        it.forEach { informe -> logger.warn { informe } }
    }



}