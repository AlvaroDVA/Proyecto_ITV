package dev.itv.itv_proyecto.models.dto

class VehiculoDto (
    val matricula : String,
    val marca : String,
    val modelo : String,
    val fechaMatricula: String,
    val fechaUltimaRevision : String,
    val tipoMotor : String,
    val tipoVehiculo: String,
    var dni : String
)