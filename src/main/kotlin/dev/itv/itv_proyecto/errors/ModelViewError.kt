package dev.itv.itv_proyecto.errors

sealed class ModelViewError (message: String) : Error(message)  {
    class GuardarError(message: String) : ModelViewError(message)
    class ActualizarError(message: String) : ModelViewError(message)
    class AccionError(message: String) : ModelViewError(message)
}