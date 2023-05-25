package dev.itv.itv_proyecto.errors

sealed class PropietarioErrors (message: String) : Error (message) {
    class PropietarioNotFoundError(message: String) : PropietarioErrors(message)
    class PropietarioQueryErrors(message: String) : PropietarioErrors(message)
}