package dev.itv.itv_proyecto.viewmodels

import com.github.michaelbull.result.*
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.ModelViewError
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.models.dto.VehiculoDto
import dev.itv.itv_proyecto.models.states.EditarState
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.PropietarioRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.repositories.VehiculoRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.validators.validarCita
import dev.itv.itv_proyecto.validators.validarCitaNueva
import dev.itv.itv_proyecto.validators.validarCitas
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.time.LocalDate

class CitaViewModel (
    val repositorioVehiculos : VehiculoRepositoryImpl,
    val repositorioInformes : InformeRepositoryImpl,
    val repositorioTrabajadores : TrabajadorRepositoryImpl,
    val repositorioPropietarios : PropietarioRepositoryImpl
) : KoinComponent {

    private val logger = KotlinLogging.logger {  }

    val listaVehiculos = FXCollections.observableArrayList<VehiculoDto>()
    val tiposMotor = FXCollections.observableArrayList<String>()
    val tiposVehiculos = FXCollections.observableArrayList<String>()
    val listaTrabajadores = FXCollections.observableArrayList<String>()
    val listaHoras = FXCollections.observableArrayList<String>()

    val state = EditarState()

    init {
        logger.warn { "Iniciando CitaViewModel " }
        cargarState()
        iniciarInterfaz()
    }

    private fun iniciarInterfaz() {
        iniciarListaVehiculos()
        iniciarMotores()
        iniciarVehiculos()
        iniciarTrabajadores()
        iniciarHoras()
    }


    /**
     * Función que carga el ComboBox del selector de Horas
     */
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

    /**
     * Función que carga la tabla de Vehiculos
     *
     * @see Mappers.toDto
     */
    private fun iniciarListaVehiculos() {
        logger.debug { "Iniciando Lista de Vehículos" }
        listaVehiculos.apply {
            clear()
            addAll(
                repositorioVehiculos.loadAll().component1()!!.map { Mappers().toVehiculoDto(it) }
            )
        }
    }


    /**
     * Función para iniciar el ComboBox de los tipos de motores mapeados a String
     */
    private fun iniciarMotores() {
        logger.debug { "Iniciando Motores" }
        tiposMotor.apply {
            clear()
            addAll(listOf("") + TipoMotor.values().map { it.toString() } )
        }
    }

    /**
     * Función para iniciar el ComboBox de los tipos de Vehiculos mapeados a String
     */
    private fun iniciarVehiculos() {
        logger.debug { "Iniciando Vehículos" }
        tiposVehiculos.apply {
            clear()
            addAll(listOf("") + TipoVehiculo.values().map { it.toString() } )
        }
    }

    /**
     * Función para iniciar la lista de todos los trabajadores
     */
    private fun iniciarTrabajadores() {
        listaTrabajadores.addAll(
            listOf("") +
                    repositorioTrabajadores.loadAll().component1()!!.map { "${it.idTrabajador} -- ${it.nombreTrabajador}" } )

    }

    fun listaFiltrada(dni : String, matricula : String): ObservableList<VehiculoDto>? {
        logger.debug { " Filtrando Lista - $dni - $matricula "}
        return listaVehiculos.filtered {
            if (dni != "") {
                it.dni.contains(dni, ignoreCase = true)
            }else {
                true
            }
        }.filtered {
            if (matricula != "") {
                it.matricula.contains(matricula, ignoreCase = true)
            }else {
                true
            }
        }
    }

    fun seleccionarVehiculo(vehiculoDto: VehiculoDto) {
        val vehiculo = repositorioVehiculos.findById(vehiculoDto.matricula).component1()!!
        state.apply {
            matriculaInforme.value = vehiculo.matricula
            marcaVehiculo.value = vehiculo.marca
            modeloVehiculo.value = vehiculo.modelo
            fechaMatriculacion.value = vehiculo.fechaMatricula
            ultimaRevision.value = vehiculo.fechaUltimaRevision
            tipoMotor.value = vehiculo.tipoMotor.toString()
            tipoVehiculo.value = vehiculo.tipoVehiculo.toString()
            dniPropietario.value = vehiculo.propietario.dni
            nombrePropietario.value = vehiculo.propietario.nombre
            apellidosPropietario.value = vehiculo.propietario.apellidos
            telefonoPropietario.value = vehiculo.propietario.telefono.toString()
            emailPropietario.value = vehiculo.propietario.emailPropietario
        }

    }

    /**
     * Función que genera asigna los valores del trabajador a la interfaz y al informe
     *
     * @see TrabajadorRepositoryImpl.loadAll
     */
    fun seleccionarTrabajador(trabajador: String) {
        val trabajadorSeleccionado = repositorioTrabajadores.loadAll().component1()!!
            .find { it.idTrabajador.toString() == trabajador.split(" -- ")[0] }

        state.idTrabajador.value = trabajadorSeleccionado?.idTrabajador?.toString() ?: ""
        state.nombreTrabajador.value = trabajadorSeleccionado?.nombreTrabajador
        state.emailTrabajador.value = trabajadorSeleccionado?.email
    }

    /**
     * Función que limpia todos los campos de la interfaz
     */
    fun limpiarCampos() {
        if (RoutesManager.action == ActionView.NEW) {
            state.dniPropietario.value = ""
            state.nombrePropietario.value = ""
            state.apellidosPropietario.value = ""
            state.telefonoPropietario.value = ""
            state.emailPropietario.value = ""
            state.idTrabajador.value = ""
            state.nombreTrabajador.value = ""
            state.emailTrabajador.value = ""
            state.idInforme.value = ""
            state.trabajadorInforme.value = ""
            state.matriculaInforme.value = ""
            state.horaCita.value = ""
            state.fechaCita.value = LocalDate.now()
            state.marcaVehiculo.value = ""
            state.modeloVehiculo.value = ""
            state.fechaMatriculacion.value = LocalDate.now()
            state.ultimaRevision.value = LocalDate.now()
            state.tipoMotor.value = ""
            state.tipoVehiculo.value = ""
        }else {
            state.trabajadorInforme.value = ""
            state.idTrabajador.value = ""
            state.nombreTrabajador.value = ""
            state.emailTrabajador.value = ""
            state.horaCita.value = ""
            state.fechaCita.value = LocalDate.now()
        }
    }

    fun botonGuardar() : Result<Informe, ModelViewError> {
        val cita  = generarInforme().onFailure {
            return Err(ModelViewError.GuardarError(it.message!!))
        }.component1()!!

        repositorioInformes.loadAll().component1()!!.validarCitas(cita).onFailure {
            return Err(ModelViewError.GuardarError(it.message!!))
        }

        if (RoutesManager.action == ActionView.NEW) {
            state.validarCitaNueva().onFailure {
                return Err(it)
            }
            repositorioInformes.saveCita(cita).onSuccess {
                return Ok(it)
            }.onFailure {
                return Err(ModelViewError.GuardarError(it.message!!))
            }
        }
        if (RoutesManager.action == ActionView.UPDATE) {
            if (state.frenadoInforme.value == "0.0") {
                repositorioInformes.updatCitaById(cita.idInforme, cita).onFailure {
                    return Err(ModelViewError.ActualizarError(it.message!!))
                }.onSuccess {
                    return Ok(it)
                }
            } else {
                repositorioInformes.updateInformeById(cita.idInforme, cita).onFailure {
                    return Err(ModelViewError.ActualizarError(it.message!!))
                }.onSuccess {
                    return Ok(it)
                }
            }

        }
        return Err(ModelViewError.GuardarError("No se ha podido guardar el informe"))
    }

    private fun generarInforme(): Result<Informe, ModelViewError > {

        state.validarCita().onFailure {
            return Err(it)
        }
        logger.error { "Fecha Matriculacion : ${state.fechaMatriculacion} " }

        return Ok(
            Mappers().informeDtoToInforme(conexion = repositorioInformes.database,
                InformeDto(
                    idInforme = state.idInforme.value?: "-1L",
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
                    frenado = state.frenadoInforme.value.toString(),
                    apto = state.apto.value.toString(),
                    luces = state.lucesInforme.value.toString(),
                    contaminacion = state.contaminacionInforme.value.toString(),
                    interior = state.interiorInforme.value.toString()
                )
            )!!
        ).also {
            logger.warn { it }
        }

    }

    /**
     * Función que carga el State compartido de la Vista Principal al State de esta vista
     */
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

}