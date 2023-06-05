package dev.itv.itv_proyecto.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.models.dto.VehiculoDto
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.viewmodels.CitaViewModel
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CitaController : KoinComponent {
    private val logger = KotlinLogging.logger {  }

    private val citaViewModel : CitaViewModel by inject()

    private val mainView : MainViewModel by inject()

    @FXML
    lateinit var matriculaBuscar : TextField
    @FXML
    lateinit var dniBuscar : TextField
    @FXML
    lateinit var tablaVehiculos : TableView<VehiculoDto>
    @FXML
    lateinit var columnaMatricula : TableColumn<VehiculoDto, String>
    @FXML
    lateinit var columnaMarca : TableColumn<VehiculoDto,String>
    @FXML
    lateinit var columnaModelo : TableColumn<VehiculoDto,String>
    @FXML
    lateinit var columnaDni : TableColumn<VehiculoDto,String>


    @FXML
    lateinit var fieldMatriculaVehiculo : TextField
    @FXML
    lateinit var fieldMarca : TextField
    @FXML
    lateinit var fieldModelo : TextField
    @FXML
    lateinit var dateFieldFechaMatriculacion : DatePicker
    @FXML
    lateinit var fieldFechaUltimaRevision : DatePicker
    @FXML
    lateinit var selectorMotor : ComboBox<String>
    @FXML
    lateinit var selectorTipoVehiculo : ComboBox<String>
    @FXML
    lateinit var fieldDNIVehiculo : TextField

    @FXML
    lateinit var textfieldIdTrabajador : TextField
    @FXML
    lateinit var fieldNombreTrabajador : TextField
    @FXML
    lateinit var fieldEmailTrabajador : TextField

    @FXML
    lateinit var fieldDNIPropietario : TextField
    @FXML
    lateinit var fieldNombrePropietario : TextField
    @FXML
    lateinit var fieldApellidosPropietario : TextField
    @FXML
    lateinit var fieldTelefonoPropietario : TextField
    @FXML
    lateinit var fieldEmailPropietario : TextField

    @FXML
    lateinit var selectorTrabajador : ComboBox<String>
    @FXML
    lateinit var selectorHora : ComboBox<String>
    @FXML
    lateinit var fieldFechaCita : DatePicker

    @FXML
    lateinit var btnGuardar : Button
    @FXML
    lateinit var btnVaciar : Button

    fun initialize() {
        iniciarBinding()
        iniciarEventos()
    }

    private fun iniciarEventos() {
        logger.warn { "Iniciando Bindings" }
        tablaVehiculos.items = citaViewModel.listaVehiculos
        columnaModelo.cellValueFactory = PropertyValueFactory("modelo")
        columnaMarca.cellValueFactory = PropertyValueFactory("marca")
        columnaMatricula.cellValueFactory = PropertyValueFactory("matricula")
        columnaDni.cellValueFactory = PropertyValueFactory("dni")

        selectorHora.items = citaViewModel.listaHoras

        dniBuscar.setOnKeyReleased {
            it?.let { onFiltrar() }
        }

        matriculaBuscar.setOnKeyReleased {
            it?.let { onFiltrar() }
        }

        tablaVehiculos.selectionModel.selectedItemProperty().addListener {_,_,newValue ->
            newValue?.let { onSeleccionarTable(it) }
        }

        selectorTrabajador.items = citaViewModel.listaTrabajadores
        selectorTrabajador.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTrabajadorSeleccionar(it) }
        }
        selectorHora.selectionModel.selectedItemProperty().addListener {_,_,newValue ->
            newValue?.let { onHoraSeleccionar(it)}
        }

        btnVaciar.setOnAction { onLimpiar() }

        btnGuardar.setOnAction { onGuardar() }

        if (RoutesManager.action == ActionView.UPDATE) {
            deshabilitarTabla()
        }
    }

    private fun deshabilitarTabla() {
        tablaVehiculos.isDisable = true
        selectorTrabajador.isDisable = false
        selectorHora.isDisable = false
        fieldFechaCita.isDisable = false
    }

    private fun onHoraSeleccionar(it: String) {
        citaViewModel.state.horaCita.value = it
    }

    private fun onTrabajadorSeleccionar(trabajador: String) {
        citaViewModel.seleccionarTrabajador(trabajador)
    }

    private fun onSeleccionarTable(vehiculo: VehiculoDto) {
        citaViewModel.seleccionarVehiculo(vehiculo)
        ponerEstilos()
        selectorTrabajador.isDisable = false
        selectorHora.isDisable = false
        fieldFechaCita.isDisable = false
    }

    val estiloEnable = "-fx-opacity: 1"

    private fun ponerEstilos() {
        fieldMatriculaVehiculo.style = estiloEnable
        fieldMarca.style = estiloEnable
        fieldModelo.style = estiloEnable
        dateFieldFechaMatriculacion.style = estiloEnable
        fieldFechaUltimaRevision.style = estiloEnable
        selectorMotor.style = estiloEnable
        selectorTipoVehiculo.style = estiloEnable
        fieldDNIVehiculo.style = estiloEnable
        fieldDNIPropietario.style = estiloEnable
        fieldNombrePropietario.style = estiloEnable
        fieldApellidosPropietario.style = estiloEnable
        fieldTelefonoPropietario.style = estiloEnable
        fieldEmailPropietario.style = estiloEnable
    }

    private fun onFiltrar() {
        tablaVehiculos.items = citaViewModel.listaFiltrada(dniBuscar.text, matriculaBuscar.text)
    }

    private fun iniciarBinding() {
        fieldDNIPropietario.textProperty().bind(citaViewModel.state.dniPropietario)
        fieldNombrePropietario.textProperty().bind(citaViewModel.state.nombrePropietario)
        fieldApellidosPropietario.textProperty().bind(citaViewModel.state.apellidosPropietario)
        fieldTelefonoPropietario.textProperty().bind(citaViewModel.state.telefonoPropietario)
        fieldEmailPropietario.textProperty().bind(citaViewModel.state.emailPropietario)
        textfieldIdTrabajador.textProperty().bind(citaViewModel.state.idTrabajador)
        fieldNombreTrabajador.textProperty().bind(citaViewModel.state.nombreTrabajador)
        fieldEmailTrabajador.textProperty().bind(citaViewModel.state.emailTrabajador)
        fieldDNIPropietario.textProperty().bind(citaViewModel.state.dniPropietario)
        fieldMarca.textProperty().bind(citaViewModel.state.marcaVehiculo)
        fieldModelo.textProperty().bind(citaViewModel.state.modeloVehiculo)
        selectorHora.valueProperty().bind(citaViewModel.state.horaCita)
        fieldFechaCita.valueProperty().bindBidirectional(citaViewModel.state.fechaCita)
        dateFieldFechaMatriculacion.valueProperty().bindBidirectional(citaViewModel.state.fechaMatriculacion)
        fieldFechaUltimaRevision.valueProperty().bind(citaViewModel.state.ultimaRevision)
        fieldDNIVehiculo.textProperty().bind(citaViewModel.state.dniPropietario)
        selectorMotor.valueProperty().bind(citaViewModel.state.tipoMotor)
        selectorTipoVehiculo.valueProperty().bind(citaViewModel.state.tipoVehiculo)
        selectorTrabajador.valueProperty().bindBidirectional(citaViewModel.state.trabajadorInforme)
        fieldMatriculaVehiculo.textProperty().bind(citaViewModel.state.matriculaInforme)
    }

    private fun onLimpiar() {
        citaViewModel.limpiarCampos()
    }
    private fun onGuardar() {
        citaViewModel.botonGuardar().onFailure {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al guardar"
                headerText = ""
                contentText = "No se ha podido guardar : ${it.message}"
                show()
            }
            mainView.iniciarInterfaz()
        }.onSuccess {
            Alert(Alert.AlertType.INFORMATION).apply {
                title = "Guardado con exito"
                headerText = ""
                contentText = "Se ha guardado con exito"
                show()
            }
            mainView.limpiarState()
            RoutesManager.cerrarEditar()
        }
    }
}