package dev.itv.itv_proyecto.models

import dev.itv.itv_proyecto.enums.Especialidad
import java.time.LocalDate
import java.util.*

data class Trabajador (
    val idTrabajador: Long = -1L,
    val nombreTrabajador: String,
    val telefonoTrabajador: Int,
    val email : String,
    val username : String,
    val password : String,
    val fechaContratacion : LocalDate,
    val especialidad : Especialidad,
    val isResponsable : Boolean
){

    private val anosTrabajados : Int get() =
        LocalDate.now().year - fechaContratacion.year

    private val salarioExtra = if (isResponsable) {
        (anosTrabajados / 3 * 100.0) + 1000.0
    }else {
        (anosTrabajados / 3 * 100.0)
    }

    val salario : Double get() =
        when (especialidad) {
            Especialidad.ADMINISTRACION ->  1650.0 + salarioExtra
            Especialidad.ELECTRICIDAD -> 1800.0 + salarioExtra
            Especialidad.MOTOR -> 1700.0 + salarioExtra
            Especialidad.MECANICA -> 1600.0 + salarioExtra
            Especialidad.INTERIOR -> 1750.0 + salarioExtra
        }
}