package dev.itv.itv_proyecto.errors

sealed class VehiculosErrors (message: String) : Error (message) {
    class VehiculoNotFoundError(message: String) : VehiculosErrors(message)
    class VehiculoConsultaError(message: String) : VehiculosErrors(message)
    class VehiculoFoundError(message: String) : VehiculosErrors(message)
}