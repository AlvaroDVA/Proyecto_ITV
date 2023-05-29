package dev.itv.itv_proyecto.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.viewmodels.EditarViewModel
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NuevoEditarController : KoinComponent{

    val editarViewModel : EditarViewModel by inject()

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
        fieldDNIPropietario.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        fieldNombrePropietario.textProperty().bindBidirectional(editarViewModel.state.nombrePropietario)
        fieldApellidosPropietario.textProperty().bindBidirectional(editarViewModel.state.apellidosPropietario)
        fieldTelefonoPropietario.textProperty().bindBidirectional(editarViewModel.state.telefonoPropietario)
        fieldEmailPropietario.textProperty().bindBidirectional(editarViewModel.state.emailPropietario)
        textfieldIdTrabajador.textProperty().bindBidirectional(editarViewModel.state.idTrabajador)
        fieldNombreTrabajador.textProperty().bindBidirectional(editarViewModel.state.nombreTrabajador)
        fieldEmailTrabajador.textProperty().bindBidirectional(editarViewModel.state.emailTrabajador)
        fieldIDInforme.textProperty().bindBidirectional(editarViewModel.state.idInforme)
        fieldDNIInforme.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        fieldFranado.textProperty().bindBidirectional(editarViewModel.state.frenadoInforme)
        fieldContaminacion.textProperty().bindBidirectional(editarViewModel.state.contaminacionInforme)
        fieldMatriculaInforme.textProperty().bindBidirectional(editarViewModel.state.matriculaInforme)
        fieldDNIInforme.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        fieldDNIPropietario.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        fieldDNIVehiculo.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        fieldMatriculaVehiculo.textProperty().bindBidirectional(editarViewModel.state.matriculaInforme)
        fieldMarca.textProperty().bindBidirectional(editarViewModel.state.marcaVehiculo)
        fieldModelo.textProperty().bindBidirectional(editarViewModel.state.modeloVehiculo)
        selectorHora.valueProperty().bindBidirectional(editarViewModel.state.horaCita)
        fieldFechaCita.valueProperty().bindBidirectional(editarViewModel.state.fechaCita)
        dateFieldFechaMatriculacion.valueProperty().bindBidirectional(editarViewModel.state.fechaMatriculacion)
        fieldFechaUltimaRevision.valueProperty().bindBidirectional(editarViewModel.state.ultimaRevision)
        checkboxApto.selectedProperty().bindBidirectional(editarViewModel.state.apto)
        checkboxLuces.selectedProperty().bindBidirectional(editarViewModel.state.lucesInforme)
        checkboxInterior.selectedProperty().bindBidirectional(editarViewModel.state.interiorInforme)
        fieldDNIInforme.textProperty().bindBidirectional(editarViewModel.state.dniPropietario)
        selectorMotor.valueProperty().bindBidirectional(editarViewModel.state.tipoMotor)
        selectorTipoVehiculo.valueProperty().bindBidirectional(editarViewModel.state.tipoVehiculo)
        selectorTrabajador.valueProperty().bindBidirectional(editarViewModel.state.trabajadorInforme)

    }

    /**
     * Función que asigna las acciones de los elementos de la Interfaz Grafica
     */
    private fun iniciarEventos() {
        btnGuardar.setOnAction { onGuardar() }
        btnVaciar.setOnAction { onLimpiar() }

        selectorHora.items = editarViewModel.listaHoras
        selectorTrabajador.items = editarViewModel.listaTrabajador
        selectorTipoVehiculo.items = editarViewModel.tiposVehiculos
        selectorMotor.items = editarViewModel.tiposMotor
        selectorTrabajador.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTrabajadorSeleccionar(it) }
        }

        fieldFechaUltimaRevision.style = estiloEnable
    }

    private fun onTrabajadorSeleccionar(trabajador: String) {
        editarViewModel.ponerTrabajador(trabajador)
    }

    /**
     * Función que guardara los datos de la interfaz Gráfica. Si él informe sé guarda o actualiza sé abrir un Alert Confirmandolo. Si da error saltará
     * un alert contando el error
     */
    private fun onGuardar() {
        editarViewModel.botonGuardar().onFailure {
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
            mainView.iniciarInterfaz()
            RoutesManager.cerrarEditar()
        }
    }

    /**
     * Función que limpia los campos de la interfaz gráfica
     */
    fun onLimpiar() {
        editarViewModel.limpiarCampos()
    }

}