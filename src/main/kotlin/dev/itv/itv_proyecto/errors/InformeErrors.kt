package dev.itv.itv_proyecto.errors

sealed class InformeErrors (message: String) : Error(message) {
    class InformeNotFoundError(message: String) : InformeErrors(message)
    class InformeQueryErrors(message: String) : InformeErrors(message)
    class InformesValidatorError(message: String) : InformeErrors(message)
}

