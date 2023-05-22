package dev.itv.itv_proyecto.models

import java.time.LocalDate
import java.util.UUID

data class Informe (
    val idInforme : UUID = UUID.randomUUID(),
    val apto : Boolean?,
    val frenado : Double?,
    val contaminacion : Double?,
    val interior : Boolean?,
    val luces : Boolean?,
    val trabajador: Trabajador,
    val vehiculo: Vehiculo,
    val propietario: Propietario = vehiculo.propietario,
    val horaCita : String?,
    val fechaCita : LocalDate?
){
}