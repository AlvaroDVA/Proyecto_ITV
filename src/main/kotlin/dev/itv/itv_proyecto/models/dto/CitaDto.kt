package dev.itv.itv_proyecto.models.dto

import com.google.gson.annotations.SerializedName

class CitaDto (
    @SerializedName("idCita") val idCita : String = "-1L",
    @SerializedName("idTrabajador") val idTrabajador: String,
    @SerializedName("nombreTrabajador") val nombreTrabajador: String,
    @SerializedName("email") val email: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("fechaMatricula") val fechaMatricula: String,
    @SerializedName("fechaUltimaRevision") val fechaUltimaRevision: String,
    @SerializedName("tipoMotor") val tipoMotor: String,
    @SerializedName("tipoVehiculo") val tipoVehiculo: String,
    @SerializedName("dni") val dni: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("emailPropietario") val emailPropietario: String,
    @SerializedName("horaCita") val horaCita: String?,
    @SerializedName("fechaCita") val fechaCita: String?
)