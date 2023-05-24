package dev.itv.itv_proyecto.errors

sealed class InformeErrors (message: String) : Error(message) {
    class InformeNotFoundError(message: String) : InformeErrors(message)
    class InformeMalFormadoError(message: String) : InformeErrors(message)
}
