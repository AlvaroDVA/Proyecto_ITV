package dev.itv.itv_proyecto.models

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import java.time.LocalDate

data class Vehiculo (
    val matricula : String,
    val marca : String,
    val modelo : String,
    val fechaMatricula: LocalDate,
    val fechaUltimaRevision : LocalDate,
    val tipoMotor : TipoMotor,
    val tipoVehiculo: TipoVehiculo,
    var propietario: Propietario
)