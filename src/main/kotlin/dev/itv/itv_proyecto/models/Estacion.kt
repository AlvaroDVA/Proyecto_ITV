package dev.itv.itv_proyecto.models

data class Estacion(
    val idEstacion: Long,
    val nombreEstacion: String,
    val direccionEstacion: String,
    val numeroTelefono : Int,
    val emailEstacion : String,
    val trabajadores : List<Trabajador> = emptyList()
) {
}