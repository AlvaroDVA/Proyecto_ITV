package dev.itv.itv_proyecto.models

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import java.time.LocalDate
import java.time.LocalDateTime

data class Vehiculo (
    val matricula : String,
    val marca : String,
    val modelo : String,
    val fechaMatricula: LocalDate,
    val fechaUltimaRevision : LocalDate,
    val tipoMotor : TipoMotor,
    val tipoVehiculo: TipoVehiculo,
    val propietario: Propietario
)