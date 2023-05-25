package dev.itv.itv_proyecto.errors

sealed class DatabaseErrors (message: String) : Error (message) {
    class consultaDatabaseError(message: String) : DatabaseErrors(message)
}