package dev.itv.itv_proyecto.viewmodels

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.errors.ModelViewError
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.models.states.EditarState
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.PropietarioRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.repositories.VehiculoRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.validators.validarCita
import dev.itv.itv_proyecto.validators.validarInforme
import mu.KotlinLogging
import java.time.LocalDate

class InformeViewModel (
    val repositorioTrabajador : TrabajadorRepositoryImpl,
    val repositorioInforme : InformeRepositoryImpl,
    val repositorioVehiculo : VehiculoRepositoryImpl,
    val repositorioPropietario : PropietarioRepositoryImpl,
){
    private val logger = KotlinLogging.logger {  }

    val  state : EditarState = EditarState()

    init {
        logger.warn { "Iniciando CitaViewModel " }
        cargarState()
    }

    fun limpiarCampos() {
        state.frenadoInforme.value = ""
        state.contaminacionInforme.value = ""
        state.interiorInforme.value = false
        state.lucesInforme.value = false
        state.apto.value = false
    }

    private fun cargarState() {
        state.apply {
            state.dniPropietario.value = RoutesManager.compartirState.dniPropietario.value
            state.nombrePropietario.value = RoutesManager.compartirState.nombrePropietario.value
            state.apellidosPropietario.value = RoutesManager.compartirState.apellidosPropietario.value
            state.telefonoPropietario.value = RoutesManager.compartirState.telefonoPropietario.value
            state. emailPropietario.value = RoutesManager.compartirState.emailPropietario.value
            state.idTrabajador.value = RoutesManager.compartirState.idTrabajador.value
            state.nombreTrabajador.value = RoutesManager.compartirState.nombreTrabajador.value
            state.emailTrabajador.value = RoutesManager.compartirState.emailTrabajador.value
            state.idInforme.value = RoutesManager.compartirState.idInforme.value.toString()
            state.frenadoInforme.value = RoutesManager.compartirState.frenadoInforme.value
            state.contaminacionInforme.value = RoutesManager.compartirState.contaminacionInforme.value
            state.trabajadorInforme.value = RoutesManager.compartirState.trabajadorInforme.value
            state.matriculaInforme.value = RoutesManager.compartirState.matriculaInforme.value
            state.horaCita.value = RoutesManager.compartirState.horaCita.value
            state.fechaCita.value = RoutesManager.compartirState.fechaCita.value
            state.interiorInforme.value = RoutesManager.compartirState.interiorInforme.value
            state.lucesInforme.value = RoutesManager.compartirState.lucesInforme.value
            state.apto.value = RoutesManager.compartirState.apto.value
            state.marcaVehiculo.value = RoutesManager.compartirState.marcaVehiculo.value
            state.modeloVehiculo.value = RoutesManager.compartirState.modeloVehiculo.value
            state.fechaMatriculacion.value = RoutesManager.compartirState.fechaMatriculacion.value
            state.ultimaRevision.value = RoutesManager.compartirState.ultimaRevision.value
            state.tipoMotor.value = RoutesManager.compartirState.tipoMotor.value
            state.tipoVehiculo.value = RoutesManager.compartirState.tipoVehiculo.value
        }.also {
            logger.warn { it}
        }
    }

    private fun generarInforme(): Result<Informe, ModelViewError> {

        state.validarCita().onFailure {
            return Err(it)
        }
        logger.error { "Fecha Matriculacion : ${state.fechaMatriculacion} " }

        return Ok(
            Mappers().informeDtoToInforme(conexion = repositorioInforme.database,
                InformeDto(
                    idTrabajador = state.idTrabajador.value,
                    nombreTrabajador = state.nombreTrabajador.value,
                    email = state.emailTrabajador.value,
                    matricula = state.matriculaInforme.value,
                    marca = state.marcaVehiculo.value,
                    modelo = state.modeloVehiculo.value,
                    fechaMatricula = state.fechaMatriculacion.value.toString(),
                    fechaUltimaRevision = LocalDate.now().toString(),
                    tipoMotor = state.tipoMotor.value.toString(),
                    tipoVehiculo = state.tipoVehiculo.value.toString(),
                    dni = state.dniPropietario.value,
                    nombre = state.nombrePropietario.value,
                    apellidos = state.apellidosPropietario.value,
                    telefono = state.telefonoPropietario.value.toString(),
                    emailPropietario = state.emailPropietario.value,
                    horaCita = state.horaCita.value,
                    fechaCita = state.fechaCita.value.toString(),
                    apto = state.apto.value.toString(),
                    contaminacion = state.contaminacionInforme.value.toString(),
                    frenado = state.frenadoInforme.value.toString(),
                    idInforme = state.idInforme.value.toString(),
                    interior = state.interiorInforme.value.toString(),
                    luces = state.lucesInforme.value.toString()
                )
            )!!
        ).also {
            logger.warn { it }
        }

    }

    fun botonGuardar(): Result<Informe, ModelViewError> {
        val informe = generarInforme().onFailure {
            return Err(it)
        }.component1()!!

        informe.validarInforme().onFailure {
            return Err(ModelViewError.GuardarError(it.message!!))
        }

        repositorioInforme.updateInformeById(informe.idInforme, informe).onFailure {
            return Err(ModelViewError.ActualizarError(it.message!!))
        }.onSuccess {
            return Ok(it)
        }

        return Err(ModelViewError.GuardarError("No se ha guardado el informe"))
    }
}