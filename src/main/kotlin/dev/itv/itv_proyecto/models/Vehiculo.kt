package dev.itv.itv_proyecto.models

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import java.time.LocalDate
import java.time.LocalDateTime

data class Vehiculo (
    val idVehiculo: Long = -1L,
    val marca : String,
    val modelo : String,
    val matricula : String,
    val fechaMatricula: LocalDate,
    val fechaUltimaRevision : LocalDateTime,
    val tipoMotor : TipoMotor,
    val tipoVehiculo: TipoVehiculo,
    val propietario: Propietario
)