package dev.itv.itv_proyecto.viewmodels

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.models.states.MainState
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
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
            addAll(listaInformes.map { Mappers().toDto(it) })
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
            it.matricula.contains(nombre.toString())
        }.filtered {
            it.tipoVehiculo.contains(tipo.toString())
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
            emailPropietario.value = informe.email
            idTrabajador.value = informe.idTrabajador
            idInforme.value = informe.idInforme
            nombreTrabajador.value = informe.nombreTrabajador
            emailTrabajador.value = informe.email
            frenadoInforme.value = informe.frenado
            contaminacionInforme.value = informe.contaminacion
            trabajadorInforme.value = "${informe.idInforme} -- ${informe.nombreTrabajador}"
            dniInforme.value = informe.dni
            horaCita.value = informe.horaCita
            fechaCita.value = LocalDate.parse(informe.fechaCita)
            matriculaInforme.value = informe.matricula
            matriculaVehiculo.value = informe.matricula
            marcaVehiculo.value = informe.marca
            modeloVehiculo.value = informe.modelo
            apto.value = informe.apto == "1"
            lucesInforme.value = informe.apto == "1"
            interiorInforme.value = informe.apto == "1"
            fechaMatriculacion.value = LocalDate.parse(informe.fechaMatricula)
            ultimaRevision.value = LocalDate.parse(informe.fechaUltimaRevision)
        }
    }

}