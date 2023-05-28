package dev.itv.itv_proyecto.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.errors.ModelViewError
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.models.states.EditarState
import java.time.LocalDate

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

fun Informe.validarInformeNuevo(): Result<Informe, InformeErrors.InformesValidatorError> {
    if (this.fechaCita == LocalDate.now()) {
        return Err(InformeErrors.InformesValidatorError("La fecha para las nuevas citas no pueden ser el mismo dia"))
    }
    if (this.fechaCita!!.isBefore(LocalDate.now())) {
        return Err(InformeErrors.InformesValidatorError("No se puede asignar una cita nueva en el pasado"))
    }
    return Ok(this)
}

fun Propietario.validar() : Result<Propietario, PropietarioErrors>{
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    val dniRegex = "[0-9]{8}[A-Za-z]".toRegex()
    if (!emailRegex.matches(this.emailPropietario)) return Err(PropietarioErrors.PropietarioValidator("El Email del propietario no cumple el formato de email"))
    if (!dniRegex.matches(this.dni)) return Err(PropietarioErrors.PropietarioValidator("El DNI no tiene formato valido"))

    return Ok(this)
}

fun Vehiculo.validar() : Result<Vehiculo, VehiculosErrors> {
    if (this.matricula.length < 6) return Err(VehiculosErrors.VehiculoValidatorError("La matricula no puede tener menor de 6 caracteres"))
    if (this.fechaUltimaRevision.isBefore(this.fechaUltimaRevision)) return Err(VehiculosErrors.VehiculoValidatorError("Un vehículo no puede tener la fecha de ultima revision antes que la de matriculación"))

    return Ok(this)
}

fun EditarState.validacionPrevia() : Result<EditarState, ModelViewError> {

    if (this.contaminacionInforme.value.contains(",")) contaminacionInforme.value.replace("," , ".")
    if (this.frenadoInforme.value.contains(",")) frenadoInforme.value.replace("," , ".")
    if (this.dniPropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al DNI del propietario"))
    if (this.nombrePropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Nombre del propietario"))
    if (this.apellidosPropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Apellido del propietario"))
    if (this.telefonoPropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Telefono del propietario"))
    if (this.emailPropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Email del propietario"))
    if (this.frenadoInforme.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Frenado del informe"))
    if (this.contaminacionInforme.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor a la Contaminación del informe"))
    if (this.trabajadorInforme.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al Trabajador del informe"))
    if (this.matriculaInforme.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor a la Matrícula del informe"))
    if (this.dniPropietario.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un valor al DNI del informe"))
    if (this.horaCita.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido una Hora en el informe"))
    if (this.marcaVehiculo.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido una Marca en el Vehiculo"))
    if (this.modeloVehiculo.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has introducido un Modelo en el Vehiculo"))
    if (this.tipoMotor.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has seleccionado un Tipo de Motor en el Vehiculo"))
    if (this.tipoVehiculo.value.isNullOrBlank()) return Err(ModelViewError.accionError("No has seleccionado un Tipo de Vehiculo en el Vehiculo"))

    return Ok(this)
}

