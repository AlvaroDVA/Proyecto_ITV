package dev.itv.itv_proyecto.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.errors.ModelViewError
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.states.EditarState
import java.time.LocalDate

/**
 * Válida los campos de informe y que estén dentro de los valores especificados, como la contaminacion o el frenado
 */


fun Informe.validarInforme() : Result<Informe, InformeErrors> {

    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    if (this.contaminacion == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado un valor valido a Contaminación"))
    if (this.contaminacion !in  (20.0 .. 50.0)) return Err(InformeErrors.InformesValidatorError("El valor de Contaminación tiene que ser entre 20.0 y 50.0"))
    if (this.frenado == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido un valor valido para Frenado"))
    if (this.frenado !in (1.0 .. 10.0)) return Err(InformeErrors.InformesValidatorError("El valor de Frenado tiene que ser entre 1.0 y 10.0"))
    if (this.apto == null) return Err(InformeErrors.InformesValidatorError("No se ha declarado el Apto o No Apto al informe"))
    if (this.interior == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado el Apto o No Apto de Interior"))
    if (this.luces == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado el Apto o No Apto de Luces"))
    if (this.fechaCita == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido una Fecha de la Cita"))
    if (this.horaCita == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido una Hora para la cita"))
    if (!emailRegex.matches(this.propietario.emailPropietario)) return Err(InformeErrors.InformesValidatorError("No se ha introducido un correo para el propietario Valido"))
    if (!emailRegex.matches(this.trabajador.email)) return Err(InformeErrors.InformesValidatorError("No se ha introducido un correo para el trabajador Valido"))
    if (this.vehiculo.fechaUltimaRevision.isBefore(vehiculo.fechaUltimaRevision)) return Err(InformeErrors.InformesValidatorError("Un vehículo no puede tener la fecha de ultima revision antes que la de matriculación"))
    if (this.propietario.telefono.toString().length != 9) return Err(InformeErrors.InformesValidatorError("El telefono tiene que tener 9 números"))
    if (this.vehiculo.fechaMatricula.isAfter(LocalDate.now())) return Err(InformeErrors.InformesValidatorError("Un vehiculo no puede haberse matriculado en el futuro "))

    return Ok(this)
}

/**
 * Validá las restricciones de citas y si un vehiculo ya ha pasado esa cita
 *
 * @param informe Informe sobre el que vamos a comparar los datos
 */
fun List<Informe>.validarCitas (informe : Informe)  : Result<Informe, InformeErrors> {

    val listaSinActual = this.filter { it.idInforme != informe.idInforme }

    listaSinActual.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
    }.takeIf { filtrado ->
        filtrado.count() < 8
    }?:let {
        return Err(InformeErrors.InformesValidatorError("Ya existen 8 citas en este intervalo"))
    }

    listaSinActual.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
                && it.trabajador.idTrabajador == informe.trabajador.idTrabajador
    }.takeIf { filtrado ->
        filtrado.count() < 4
    }?:let {
        return Err(InformeErrors.InformesValidatorError("Este trabajador ya tiene 4 citas en este intervalo"))
    }

    listaSinActual.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
                && it.vehiculo.matricula == informe.vehiculo.matricula
    }.takeIf {
        it.isEmpty()
    }?: return Err(InformeErrors.InformesValidatorError("Este vehiculo ya tiene una cita en esta fecha y hora"))

    return Ok(informe)
}

fun EditarState.validarCita () : Result<EditarState,ModelViewError> {
    if (this.dniPropietario.value.isNullOrBlank()) return Err(ModelViewError.AccionError("No se ha seleccionado un Vehiculo"))
    if (this.matriculaInforme.value.isNullOrBlank()) return Err(ModelViewError.AccionError("No has seleccionado un Vehículo"))
    if (this.trabajadorInforme.value.isNullOrBlank()) return Err(ModelViewError.AccionError("No has seleccionado un Trabajador"))
    if (this.horaCita.value.isNullOrBlank()) return Err(ModelViewError.AccionError("No has seleccionado una Hora para la Cita"))
    if (this.fechaCita.value == null) return Err(ModelViewError.AccionError("No has seleccionado una Fecha para la Cita"))

    return Ok(this)
}

fun EditarState.validarCitaNueva (): Result<EditarState, ModelViewError> {
    if (this.fechaCita.value.isEqual(LocalDate.now())) return Err(ModelViewError.AccionError("Las nuevas Citas no pueden tener de fecha el dia de Hoy"))
    if (this.fechaCita.value.isBefore(LocalDate.now())) return Err(ModelViewError.AccionError("Las nuevas Citas no pueden ocurrir en el pasado"))
    return Ok(this)
}


