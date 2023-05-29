package dev.itv.itv_proyecto.viewmodels

import dev.itv.itv_proyecto.enums.ActionExportar
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.models.states.MainState
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.storages.CsvTrabajadoresStorage
import dev.itv.itv_proyecto.services.storages.HtmlInformesStorage
import dev.itv.itv_proyecto.services.storages.JsonInformesStorage
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import mu.KotlinLogging
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }
class MainViewModel (
    val repositorioTrabajador : TrabajadorRepositoryImpl,
    val repositorioInforme : InformeRepositoryImpl,
    val csvStorage : CsvTrabajadoresStorage,
    val htmlStorage : HtmlInformesStorage,
    val jsonStorage : JsonInformesStorage
) {

    val listaInformes = FXCollections.observableArrayList<Informe>()
    val listaInformesDto = FXCollections.observableArrayList<InformeDto>()
    val tiposMotor = FXCollections.observableArrayList<String>()
    val tiposVehiculos = FXCollections.observableArrayList<String>()
    val listaTrabajadores = FXCollections.observableArrayList<Trabajador>()
    val listaHoras = FXCollections.observableArrayList<String>()

    val state = MainState()

    init {
        logger.info { "Iniciando MainModelView" }

        iniciarInterfaz()
    }

    fun iniciarInterfaz() {
        iniciarInformes()
        iniciarListaInformesDto()
        iniciarMotores()
        iniciarVehiculos()
        iniciarTrabajadores()
        iniciarHoras()
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

    private fun iniciarListaInformesDto() {
        logger.debug { "Iniciando Lista de Informes Dto" }
        listaInformesDto.apply {
            clear()
            addAll(
                listaInformes.map { Mappers().toDto(it) })
        }
    }


    private fun iniciarInformes() {
        logger.debug { "Iniciando Informes" }
        listaInformes.apply {
            clear()
            addAll(repositorioInforme.loadAll().component1()!!)
        }
    }

    private fun iniciarMotores() {
        logger.debug { "Iniciando Motores" }
        tiposMotor.apply {
            clear()
            addAll(listOf("") + TipoMotor.values().map { it.toString() } )
        }
    }

    private fun iniciarVehiculos() {
        logger.debug { "Iniciando Vehículos" }
        tiposVehiculos.apply {
            clear()
            addAll(listOf("") + TipoVehiculo.values().map { it.toString() } )
        }
    }

    private fun iniciarTrabajadores() {
        logger.debug { "Iniciando Trabajadores" }
        listaTrabajadores.apply {
            clear()
            addAll(repositorioTrabajador.loadAll().component1()!!)
        }
    }

    fun listaFiltrada(nombre: String?, motor: String?, tipo: String?): ObservableList<InformeDto>? {
        logger.debug { " Filtrando Lista - $nombre - $motor - $tipo " }
        return listaInformesDto.filtered {
            if (motor != null && motor != "") {
                it.tipoMotor.contains(motor)
            }else {
                true
            }
        }.filtered {
            it.matricula.contains(nombre!!, ignoreCase = true)
        }.filtered {
            if (tipo != null && tipo != "") {
                it.tipoVehiculo.contains(tipo)
            }else {
                true
            }
        }
    }

    fun seleccionarInforme(informe: InformeDto) {
        state.apply {
            dniVehiculo.value = informe.dni
            dniPropietario.value = informe.dni
            dniInforme.value = informe.dni
            nombrePropietario.value = informe.nombre
            apellidosPropietario.value = informe.apellidos
            telefonoPropietario.value = informe.telefono
            emailPropietario.value = informe.emailPropietario
            idTrabajador.value = informe.idTrabajador
            idInforme.value = informe.idInforme
            nombreTrabajador.value = informe.nombreTrabajador
            emailTrabajador.value = informe.email
            frenadoInforme.value = informe.frenado
            contaminacionInforme.value = informe.contaminacion
            trabajadorInforme.value = "${informe.idTrabajador} -- ${informe.nombreTrabajador}"
            dniInforme.value = informe.dni
            horaCita.value = informe.horaCita
            fechaCita.value = LocalDate.parse(informe.fechaCita)
            matriculaInforme.value = informe.matricula
            matriculaVehiculo.value = informe.matricula
            marcaVehiculo.value = informe.marca
            modeloVehiculo.value = informe.modelo
            apto.value = informe.apto == "true"
            lucesInforme.value = informe.luces == "true"
            interiorInforme.value = informe.interior == "true"
            fechaMatriculacion.value = LocalDate.parse(informe.fechaMatricula)
            ultimaRevision.value = LocalDate.parse(informe.fechaUltimaRevision)
            dniVehiculo.value = informe.dni
            tipoMotor.value = informe.tipoMotor
            tipoVehiculo.value = informe.tipoVehiculo
        }
    }

    private fun limpiarState() {
        state.apply {
            this.dniPropietario.value = ""
            this.nombrePropietario.value = ""
            this.apellidosPropietario.value = ""
            this.telefonoPropietario.value = ""
            this.emailPropietario.value = ""
            this.idTrabajador.value = ""
            this.nombreTrabajador.value = ""
            this.emailTrabajador.value = ""
            this.idInforme.value = ""
            this.frenadoInforme.value = ""
            this.contaminacionInforme.value = ""
            this.trabajadorInforme.value = ""
            this.matriculaInforme.value = ""
            this.dniInforme.value = ""
            this.horaCita.value = ""
            this.fechaCita.value = LocalDate.now()
            this.interiorInforme.value = false
            this.lucesInforme.value = false
            this.apto.value = false
            this.matriculaVehiculo.value = ""
            this.marcaVehiculo.value = ""
            this.modeloVehiculo.value = ""
            this.fechaMatriculacion.value = LocalDate.now()
            this.ultimaRevision.value = LocalDate.now()
            this.tipoMotor.value = ""
            this.tipoVehiculo.value = ""
            this.dniVehiculo.value = ""
        }
        iniciarInterfaz()
    }

    fun cambiarVentana(action : ActionView) {
        if (action == ActionView.NEW) {
            RoutesManager.compartirState.apply {
                this.dniPropietario.value = ""
                this.nombrePropietario.value = ""
                this.apellidosPropietario.value = ""
                this.telefonoPropietario.value = ""
                this.emailPropietario.value = ""
                this.idTrabajador.value = ""
                this.nombreTrabajador.value = ""
                this.emailTrabajador.value = ""
                this.idInforme.value = ""
                this.frenadoInforme.value = ""
                this.contaminacionInforme.value = ""
                this.trabajadorInforme.value = ""
                this.matriculaInforme.value = ""
                this.dniInforme.value = ""
                this.horaCita.value = ""
                this.fechaCita.value = LocalDate.now()
                this.interiorInforme.value = false
                this.lucesInforme.value = false
                this.apto.value = false
                this.matriculaVehiculo.value = ""
                this.marcaVehiculo.value = ""
                this.modeloVehiculo.value = ""
                this.fechaMatriculacion.value = LocalDate.now()
                this.ultimaRevision.value = LocalDate.now()
                this.tipoMotor.value = ""
                this.tipoVehiculo.value = ""
                this.dniVehiculo.value = ""
                RoutesManager.action = ActionView.NEW
                logger.warn {  RoutesManager.action.toString() }
            }
            limpiarState()
        }
        if (action == ActionView.UPDATE) {
            RoutesManager.compartirState.apply {
                dniPropietario.value = state.dniPropietario.value
                nombrePropietario.value = state.nombrePropietario.value
                apellidosPropietario.value = state.apellidosPropietario.value
                telefonoPropietario.value = state.telefonoPropietario.value
                emailPropietario.value = state.emailPropietario.value
                idTrabajador.value = state.idTrabajador.value
                nombreTrabajador.value = state.nombreTrabajador.value
                emailTrabajador.value = state.emailTrabajador.value
                idInforme.value = state.idInforme.value
                frenadoInforme.value = state.frenadoInforme.value
                contaminacionInforme.value = state.contaminacionInforme.value
                trabajadorInforme.value = state.trabajadorInforme.value
                matriculaInforme.value = state.matriculaInforme.value
                dniInforme.value = state.dniInforme.value
                horaCita.value = state.horaCita.value
                fechaCita.value = state.fechaCita.value
                interiorInforme.value = state.interiorInforme.value
                lucesInforme.value = state.lucesInforme.value
                apto.value = state.apto.value
                matriculaVehiculo.value = state.matriculaVehiculo.value
                marcaVehiculo.value = state.marcaVehiculo.value
                modeloVehiculo.value = state.modeloVehiculo.value
                fechaMatriculacion.value = state.fechaMatriculacion.value
                ultimaRevision.value = state.ultimaRevision.value
                tipoMotor.value = state.tipoMotor.value
                tipoVehiculo.value = state.tipoVehiculo.value
                dniVehiculo.value = state.dniVehiculo.value
                RoutesManager.action = ActionView.UPDATE
                logger.warn {  RoutesManager.action.toString() }
            }
            limpiarState()
        }
        RoutesManager.editarVentana()
    }

    fun eliminarInforme() {
        repositorioInforme.deleteInformeById(state.idInforme.value.toLong())
        iniciarInterfaz()
        limpiarState()
    }

    fun guardarArchivo(absolutePath: String, accion: ActionExportar) {
        when (accion) {
            ActionExportar.EXPORTAR_HTML -> htmlStorage.saveFile(repositorioInforme.loadAll().component1()!! , absolutePath)
            ActionExportar.EXPORTAR_CSV -> csvStorage.saveFile(repositorioTrabajador.loadAll().component1()!!, absolutePath)
            ActionExportar.EXPORTAR_JSON -> jsonStorage.saveFile(repositorioInforme.loadAll().component1()!!, absolutePath)
        }
    }

}