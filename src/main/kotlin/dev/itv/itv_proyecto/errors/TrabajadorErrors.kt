package dev.itv.itv_proyecto.errors

sealed class TrabajadorErrors (message: String) : Error(message) {
    class TrabajadorNotFoundError(message: String) : TrabajadorErrors(message)
}