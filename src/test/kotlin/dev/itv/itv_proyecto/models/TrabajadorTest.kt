package dev.itv.itv_proyecto.models

import dev.itv.itv_proyecto.enums.Especialidad
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TrabajadorTest {

    @Test
    fun getSalarioTest() {

        val administracion = Trabajador(
            nombreTrabajador = "Juan Pérez",
            telefonoTrabajador = 123456789,
            email = "juan.perez@example.com",
            username = "juanperez",
            password = "password",
            fechaContratacion = LocalDate.of(2020, 5, 15),
            especialidad = Especialidad.ADMINISTRACION,
            isResponsable = true
        )

        val electricidad = Trabajador(
            nombreTrabajador = "María López",
            telefonoTrabajador = 987654321,
            email = "maria.lopez@example.com",
            username = "marialopez",
            password = "password",
            fechaContratacion = LocalDate.of(2018, 10, 1),
            especialidad = Especialidad.ELECTRICIDAD,
            isResponsable = false
        )

        val motor = Trabajador(
            nombreTrabajador = "Carlos Rodríguez",
            telefonoTrabajador = 555555555,
            email = "carlos.rodriguez@example.com",
            username = "carlosrodriguez",
            password = "password",
            fechaContratacion = LocalDate.of(2019, 3, 20),
            especialidad = Especialidad.MOTOR,
            isResponsable = true
        )

        val mecanica = Trabajador(
            nombreTrabajador = "Laura Gómez",
            telefonoTrabajador = 111111111,
            email = "laura.gomez@example.com",
            username = "lauragomez",
            password = "password",
            fechaContratacion = LocalDate.of(2021, 8, 5),
            especialidad = Especialidad.MECANICA,
            isResponsable = false
        )

        val interior = Trabajador(
            nombreTrabajador = "Pedro Torres",
            telefonoTrabajador = 999999999,
            email = "pedro.torres@example.com",
            username = "pedrotorres",
            password = "password",
            fechaContratacion = LocalDate.of(2017, 6, 10),
            especialidad = Especialidad.INTERIOR,
            isResponsable = true
        )


        val salarioAdministracion = administracion.salario 
        val salarioElectricidad = electricidad.salario
        val salarioMotor = motor.salario
        val salarioMecanica = mecanica.salario
        val salarioInterior = interior.salario

        assertEquals(2750.0 ,salarioAdministracion)
        assertEquals( 1900.0, salarioElectricidad)
        assertEquals(2800.0, salarioMotor)
        assertEquals(1600.0 , salarioMecanica)
        assertEquals(2950.0, salarioInterior)

    }
}