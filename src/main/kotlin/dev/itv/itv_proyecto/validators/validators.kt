package dev.itv.itv_proyecto.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.models.Informe

fun List<Informe>.validarInformes(informe : Informe) : Result<Informe, InformeErrors> {

    this.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
    }.takeIf { filtrado ->
        filtrado.count() < 8
    }?:let {
        return Err(InformeErrors.InformesValidatorError("Ya existen 8 citas en este intervalo"))
    }

    this.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
                && it.trabajador.idTrabajador == informe.trabajador.idTrabajador
    }.takeIf { filtrado ->
        filtrado.count() < 4
    }?:let {
        return Err(InformeErrors.InformesValidatorError("Este trabajador ya tiene 4 citas en este intervalo"))
    }

    this.filter {
        it.horaCita == informe.horaCita && it.fechaCita == informe.fechaCita
                && it.vehiculo.matricula == informe.vehiculo.matricula
    }.takeIf {
        it.isEmpty()
    }?: return Err(InformeErrors.InformesValidatorError("Este vehiculo ya ha pasado "))

    return Ok(informe)
}

fun Informe.validarInforme() : Result<Informe, InformeErrors> {

    if (this.contaminacion == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado un valor a Contaminación"))
    if (this.contaminacion !in  (20.0 .. 50.0)) return Err(InformeErrors.InformesValidatorError("El valor de Contaminación tiene que ser entre 20.0 y 50.0"))
    if (this.frenado == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido un valor para Frenado"))
    if (this.frenado !in (1.0 .. 10.0)) return Err(InformeErrors.InformesValidatorError("El valor de Frenado tiene que ser entre 1.0 y 10.0"))
    if (this.apto == null) return Err(InformeErrors.InformesValidatorError("No se ha declarado el Apto o No Apto al informe"))
    if (this.interior == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado el Apto o No Apto de Interior"))
    if (this.luces == null) return Err(InformeErrors.InformesValidatorError("No se ha asignado el Apto o No Apto de Luces"))
    if (this.fechaCita == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido una Fecha de la Cita"))
    if (this.horaCita == null) return Err(InformeErrors.InformesValidatorError("No se ha introducido una Hora para la cita"))

    return Ok(this)
}