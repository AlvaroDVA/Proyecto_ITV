package dev.itv.itv_proyecto.models

import java.time.LocalDate

data class Informe (
    val idInforme : Long = -1L,
    val apto : Boolean?,
    val frenado : Double?,
    val contaminacion : Double?,
    val interior : Boolean?,
    val luces : Boolean?,
    val trabajador: Trabajador,
    val vehiculo: Vehiculo,
    var propietario: Propietario = vehiculo.propietario,
    val horaCita : String?,
    val fechaCita : LocalDate?
){

}