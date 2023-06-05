package dev.itv.itv_proyecto.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.viewmodels.InformeViewModel
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InformeController : KoinComponent {
    val informeViewModel : InformeViewModel by inject()

    @FXML
    private lateinit var fieldDNIPropietario : TextField
    @FXML
    private lateinit var fieldNombrePropietario : TextField
    @FXML
    private lateinit var fieldApellidosPropietario : TextField
    @FXML
    private lateinit var fieldTelefonoPropietario : TextField
    @FXML
    private lateinit var fieldEmailPropietario : TextField

    @FXML
    private lateinit var textfieldIdTrabajador : TextField
    @FXML
    private lateinit var fieldNombreTrabajador : TextField
    @FXML
    private lateinit var fieldEmailTrabajador : TextField

    @FXML
    private lateinit var fieldIDInforme : TextField
    @FXML
    private lateinit var fieldFranado : TextField
    @FXML
    private lateinit var fieldContaminacion : TextField
    @FXML
    private lateinit var selectorTrabajador : ComboBox<String>
    @FXML
    private lateinit var fieldMatriculaInforme : TextField
    @FXML
    private lateinit var fieldDNIInforme : TextField
    @FXML
    private lateinit var selectorHora : ComboBox<String>
    @FXML
    private lateinit var fieldFechaCita : DatePicker
    @FXML
    private lateinit var checkboxInterior : CheckBox
    @FXML
    private lateinit var checkboxLuces : CheckBox
    @FXML
    private lateinit var checkboxApto : CheckBox

    @FXML
    private lateinit var fieldMatriculaVehiculo : TextField
    @FXML
    private lateinit var fieldMarca : TextField
    @FXML
    private lateinit var fieldModelo : TextField
    @FXML
    private lateinit var  dateFieldFechaMatriculacion : DatePicker
    @FXML
    private lateinit var fieldFechaUltimaRevision : DatePicker
    @FXML
    private lateinit var selectorMotor : ComboBox<String>
    @FXML
    private lateinit var selectorTipoVehiculo : ComboBox<String>
    @FXML
    private lateinit var fieldDNIVehiculo : TextField
    @FXML
    private lateinit var btnGuardar : Button
    @FXML
    private lateinit var btnVaciar : Button

    @FXML
    fun initialize() {

        iniciarBindings()

        iniciarEventos()

    }

    private val mainView : MainViewModel by inject()

    val estiloEnable = "-fx-opacity: 1"

    /**
     * Funcion que inicia los Binding. Son bidireccinales porque los datos que escribamos queremos que sé guarden en él State para su posterior guardado
     */
    private fun iniciarBindings() {
        fieldDNIPropietario.textProperty().bind(informeViewModel.state.dniPropietario)
        fieldNombrePropietario.textProperty().bind(informeViewModel.state.nombrePropietario)
        fieldApellidosPropietario.textProperty().bind(informeViewModel.state.apellidosPropietario)
        fieldTelefonoPropietario.textProperty().bind(informeViewModel.state.telefonoPropietario)
        fieldEmailPropietario.textProperty().bind(informeViewModel.state.emailPropietario)
        textfieldIdTrabajador.textProperty().bind(informeViewModel.state.idTrabajador)
        fieldNombreTrabajador.textProperty().bind(informeViewModel.state.nombreTrabajador)
        fieldEmailTrabajador.textProperty().bind(informeViewModel.state.emailTrabajador)
        fieldIDInforme.textProperty().bind(informeViewModel.state.idInforme)
        fieldDNIInforme.textProperty().bind(informeViewModel.state.dniPropietario)
        fieldFranado.textProperty().bindBidirectional(informeViewModel.state.frenadoInforme)
        fieldContaminacion.textProperty().bindBidirectional(informeViewModel.state.contaminacionInforme)
        fieldMatriculaInforme.textProperty().bind(informeViewModel.state.matriculaInforme)
        fieldDNIInforme.textProperty().bind(informeViewModel.state.dniPropietario)
        fieldDNIPropietario.textProperty().bind(informeViewModel.state.dniPropietario)
        fieldDNIVehiculo.textProperty().bind(informeViewModel.state.dniPropietario)
        fieldMatriculaVehiculo.textProperty().bind(informeViewModel.state.matriculaInforme)
        fieldMarca.textProperty().bind(informeViewModel.state.marcaVehiculo)
        fieldModelo.textProperty().bind(informeViewModel.state.modeloVehiculo)
        selectorHora.valueProperty().bind(informeViewModel.state.horaCita)
        fieldFechaCita.valueProperty().bind(informeViewModel.state.fechaCita)
        dateFieldFechaMatriculacion.valueProperty().bind(informeViewModel.state.fechaMatriculacion)
        fieldFechaUltimaRevision.valueProperty().bind(informeViewModel.state.ultimaRevision)
        checkboxApto.selectedProperty().bindBidirectional(informeViewModel.state.apto)
        checkboxLuces.selectedProperty().bindBidirectional(informeViewModel.state.lucesInforme)
        checkboxInterior.selectedProperty().bindBidirectional(informeViewModel.state.interiorInforme)
        fieldDNIInforme.textProperty().bind(informeViewModel.state.dniPropietario)
        selectorMotor.valueProperty().bind(informeViewModel.state.tipoMotor)
        selectorTipoVehiculo.valueProperty().bind(informeViewModel.state.tipoVehiculo)
        selectorTrabajador.valueProperty().bind(informeViewModel.state.trabajadorInforme)

    }

    /**
     * Función que asigna las acciones de los elementos de la Interfaz Grafica
     */
    private fun iniciarEventos() {
        btnGuardar.setOnAction { onGuardar() }
        btnVaciar.setOnAction { onLimpiar() }
        ponerEstilos()
    }

    private fun ponerEstilos() {
        selectorHora.style = estiloEnable
        fieldFechaCita.style = estiloEnable
        dateFieldFechaMatriculacion.style = estiloEnable
        fieldFechaUltimaRevision.style = estiloEnable
        fieldModelo.style = estiloEnable
        fieldMarca.style = estiloEnable
    }

    /**
     * Función que guardara los datos de la interfaz Gráfica. Si él informe sé guarda o actualiza sé abrir un Alert Confirmandolo. Si da error saltará
     * un alert contando el error
     */
    private fun onGuardar() {
        informeViewModel.botonGuardar().onFailure {
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

    /**
     * Función que limpia los campos de la interfaz gráfica
     */
    fun onLimpiar() {
        informeViewModel.limpiarCampos()
    }
}