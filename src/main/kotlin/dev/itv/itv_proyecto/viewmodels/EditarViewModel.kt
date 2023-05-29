package dev.itv.itv_proyecto.viewmodels

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.ModelViewError
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.models.states.EditarState
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.PropietarioRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.repositories.VehiculoRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.storages.CsvTrabajadoresStorage
import dev.itv.itv_proyecto.services.storages.HtmlInformesStorage
import dev.itv.itv_proyecto.services.storages.JsonInformesStorage
import dev.itv.itv_proyecto.validators.*
import javafx.collections.FXCollections
import mu.KotlinLogging
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }
class EditarViewModel (
    val repositorioTrabajador : TrabajadorRepositoryImpl,
    val repositorioInforme : InformeRepositoryImpl,
    val repositorioVehiculo : VehiculoRepositoryImpl,
    val repositorioPropietario : PropietarioRepositoryImpl,
    val csvStorage : CsvTrabajadoresStorage,
    val htmlStorage : HtmlInformesStorage,
    val jsonStorage : JsonInformesStorage
) {

    val  state : EditarState = EditarState()

    val listaTrabajador = FXCollections.observableArrayList<String>()
    val listaHoras = FXCollections.observableArrayList<String>()
    val tiposMotor = FXCollections.observableArrayList<String>()
    val tiposVehiculos = FXCollections.observableArrayList<String>()

    init {
        cargarState()
        iniciarTrabajadores()
        iniciarHoras()
        iniciarMotores()
        iniciarTipos()
    }

    private fun iniciarTipos() {
        tiposVehiculos.addAll(
            listOf("") + TipoVehiculo.values().map { it.toString() }
        )
    }

    private fun iniciarMotores() {
        tiposMotor.addAll(
            listOf("") + TipoMotor.values().map { it.toString() }
        )
    }

    private fun iniciarTrabajadores() {
        listaTrabajador.addAll(
            listOf("") +
                    repositorioTrabajador.loadAll().component1()!!.map { "${it.idTrabajador} -- ${it.nombreTrabajador}" } )

    }

    private fun iniciarHoras() {
        logger.debug { "Iniciando Lista de Horas" }
        listaHoras.apply {
            clear()
            addAll(
                listOf(
                    "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                    "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
                    "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00",
                    "21:30", "22:00"
                )
            )
        }
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
            state.idInforme.value = RoutesManager.compartirState.idInforme.value
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

    fun botonGuardar() : Result<Informe, ModelViewError> {
        if (RoutesManager.action == ActionView.NEW) {
            return guardarInforme()
        }
        if (RoutesManager.action == ActionView.UPDATE) {
            return actualizarInforme()
        }
        return Err(ModelViewError.AccionError("No hay una eleccion error"))
    }

    private fun guardarInforme() : Result<Informe, ModelViewError> {
        val informe = generarInforme().onFailure {
            return Err(it)
        }.component1()!!

        informe.propietario.validar().onFailure {
            return Err(ModelViewError.GuardarError(it.message!!))
        }

        informe.vehiculo.validar().onFailure {
            return Err(ModelViewError.GuardarError(it.message!!))
        }

        repositorioPropietario.findById(informe.propietario.dni).onSuccess {
            if (!comprobarDatosPropietario(it, informe)) {
                return Err(ModelViewError.ActualizarError("Los datos de este propietario no coinciden con los reales"))
            }
        }

        repositorioPropietario.savePropietario(informe.propietario).onSuccess {
            repositorioVehiculo.save(informe.vehiculo).onFailure {
                return Err(ModelViewError.ActualizarError(it.message!!))
            }
        }.onFailure { _, ->
            repositorioVehiculo.updateVehiculoById(informe.vehiculo.matricula, informe.vehiculo).onFailure {
                repositorioVehiculo.save(informe.vehiculo).onFailure {
                    return Err(ModelViewError.ActualizarError(it.message!!))
                }
            }
        }

        repositorioInforme.loadAll().onFailure { listaInformes ->
            return Err(ModelViewError.GuardarError(listaInformes.message!!))
        }.component1()!!.validarInformes(informe).onFailure { errorInforme ->
            return Err(ModelViewError.GuardarError(errorInforme.message!!))
        }.onSuccess { _ ->
            informe.validarInforme().onFailure { errorInforme ->
                return Err(ModelViewError.GuardarError(errorInforme.message!!))
            }.onSuccess { _ ->
                informe.validarInformeNuevo().onFailure { errorInforme ->
                    return Err(ModelViewError.GuardarError(errorInforme.message!!))
                }.onSuccess { _->
                    repositorioInforme.saveInforme(informe).onFailure {errorInforme ->
                        return Err(ModelViewError.GuardarError(errorInforme.message!!))
                    }.onSuccess {
                        return Ok(it)
                    }
                }

            }
        }
        return Err(ModelViewError.GuardarError("No se ha podido guardar el informe"))
    }



    private fun actualizarInforme() : Result<Informe, ModelViewError> {
        val informe = generarInforme().onFailure {
            return Err(it)
        }.component1()!!

        informe.vehiculo.validar().onFailure {
            return Err(ModelViewError.ActualizarError(it.message!!))
        }

        informe.propietario.validar().onFailure {
            return Err(ModelViewError.ActualizarError(it.message!!))
        }

        repositorioPropietario.findById(informe.propietario.dni).onSuccess {
            if (!comprobarDatosPropietario(it, informe)) {
                return Err(ModelViewError.ActualizarError("Los datos de este propietario no coinciden con los reales"))
            }
        }

        repositorioPropietario.savePropietario(informe.propietario).onSuccess {
            repositorioVehiculo.save(informe.vehiculo).onFailure {
                return Err(ModelViewError.ActualizarError(it.message!!))
            }
        }.onFailure { _, ->
            repositorioVehiculo.updateVehiculoById(informe.vehiculo.matricula, informe.vehiculo).onFailure {
                repositorioVehiculo.save(informe.vehiculo).onFailure {
                    return Err(ModelViewError.ActualizarError(it.message!!))
                }
            }
        }

        repositorioInforme.loadAll().onFailure { listaInformes ->
            return Err(ModelViewError.GuardarError(listaInformes.message!!))
        }.component1()!!.let{ _ ->
            informe.validarInforme().onFailure { errorInforme ->
                return Err(ModelViewError.GuardarError(errorInforme.message!!))
            }.onSuccess { _ ->
                repositorioInforme.updateInformeById(informe.idInforme, informe).onFailure {errorInforme ->
                    return Err(ModelViewError.GuardarError(errorInforme.message!!))
                }.onSuccess {
                    return Ok(it)
                }
            }
        }

        return Err(ModelViewError.ActualizarError("No se ha podido actualizar el informe"))
    }

    private fun comprobarDatosPropietario(
        it: Propietario,
        informe: Informe,
    ) = it.emailPropietario.equals(informe.propietario.emailPropietario, ignoreCase = true) &&
            it.dni.equals(informe.propietario.dni, ignoreCase = true) &&
            it.nombre.equals(informe.propietario.nombre, ignoreCase = true) &&
            it.apellidos.equals(informe.propietario.apellidos, ignoreCase = true) &&
            it.telefono == informe.propietario.telefono

    private fun generarInforme(): Result<Informe, ModelViewError > {

        state.validacionPrevia().onFailure {
            return Err(it)
        }

        logger.error { "Fecha Matriculacion : ${state.fechaMatriculacion} " }

        return Ok(Mappers().informeDtoToInforme(conexion = repositorioInforme.database,
            InformeDto(
                idInforme = state.idInforme.value,
                apto = if (state.apto.value) "true" else "false" ,
                frenado = state.frenadoInforme.value.toString(),
                contaminacion = state.contaminacionInforme.value.toString(),
                interior = if (state.interiorInforme.value) "true" else "false",
                luces = if (state.lucesInforme.value) "true" else "false",
                idTrabajador = state.idTrabajador.value.toString(),
                nombreTrabajador = state.nombreTrabajador.value.toString(),
                email = state.emailTrabajador.value.toString(),
                matricula = state.matriculaInforme.value.toString(),
                marca = state.marcaVehiculo.value.toString(),
                modelo = state.modeloVehiculo.value.toString(),
                fechaMatricula = state.fechaMatriculacion.value.toString(),
                fechaUltimaRevision = LocalDate.now().toString(),
                tipoMotor = state.tipoMotor.value.toString(),
                tipoVehiculo = state.tipoVehiculo.value.toString(),
                dni = state.dniPropietario.value.toString(),
                nombre = state.nombrePropietario.value.toString(),
                apellidos = state.apellidosPropietario.value.toString(),
                telefono = state.telefonoPropietario.value.toString(),
                emailPropietario = state.emailPropietario.value.toString(),
                horaCita = state.horaCita.value.toString(),
                fechaCita = state.fechaCita.value.toString()
            )
        )!!).also {
            logger.warn { it }
        }

    }

    fun ponerTrabajador(trabajador: String) {
        val trabajadorSeleccionado = repositorioTrabajador.loadAll().component1()!!
            .find { it.idTrabajador.toString() == trabajador.split(" -- ")[0] }

        state.idTrabajador.value = trabajadorSeleccionado?.idTrabajador?.toString() ?: ""
        state.nombreTrabajador.value = trabajadorSeleccionado?.nombreTrabajador
        state.emailTrabajador.value = trabajadorSeleccionado?.email
    }

    fun limpiarCampos() {
        state.dniPropietario.value = ""
        state.nombrePropietario.value = ""
        state.apellidosPropietario.value = ""
        state.telefonoPropietario.value = ""
        state.emailPropietario.value = ""
        state.idTrabajador.value = ""
        state.nombreTrabajador.value = ""
        state.emailTrabajador.value = ""
        state.idInforme.value = ""
        state.frenadoInforme.value = ""
        state.contaminacionInforme.value = ""
        state.trabajadorInforme.value = ""
        state.matriculaInforme.value = ""
        state.horaCita.value = ""
        state.fechaCita.value = LocalDate.now()
        state.interiorInforme.value = false
        state.lucesInforme.value = false
        state.apto.value = false
        state.marcaVehiculo.value = ""
        state.modeloVehiculo.value = ""
        state.fechaMatriculacion.value = LocalDate.now()
        state.ultimaRevision.value = LocalDate.now()
        state.tipoMotor.value = ""
        state.tipoVehiculo.value = ""
    }

}