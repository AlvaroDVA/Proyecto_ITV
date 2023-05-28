package dev.itv.itv_proyecto.controllers

import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {  }
class IndexController : KoinComponent{

    val mainViewModel : MainViewModel by inject()
    @FXML
    private lateinit var menuExportarJSON : MenuItem
    @FXML
    private lateinit var menuExportarHTML : MenuItem
    @FXML
    private lateinit var menuAcercaDe : MenuItem
    @FXML
    private lateinit var buscadorNombre : TextField
    @FXML
    private lateinit var buscadorTipoVehiculo : ComboBox<String>
    @FXML
    private lateinit var buscadorMotor : ComboBox<String>

    @FXML
    private lateinit var tableInformes : TableView<InformeDto>
    @FXML
    private lateinit var tableColumnDNI : TableColumn<InformeDto, String>
    @FXML
    private lateinit var tableColumNombre : TableColumn<InformeDto,String>
    @FXML
    private lateinit var tableColumnApellidos : TableColumn<InformeDto,String>
    @FXML
    private lateinit var tableColumnTelefono : TableColumn<InformeDto,Int>
    @FXML
    private lateinit var tableColumnMatricula : TableColumn<InformeDto,String>
    @FXML
    private lateinit var tableColumnTipoVehiculo : TableColumn<InformeDto,TipoVehiculo>
    @FXML
    private lateinit var tableColumnTipoMotor : TableColumn<InformeDto,TipoMotor>
    @FXML
    private lateinit var tableColumnIDTrabajador : TableColumn<InformeDto, Long>
    @FXML
    private lateinit var tableColumnResultado : TableColumn<InformeDto,Boolean?>

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
    private lateinit var fieldNombreTrabajador : TextField
    @FXML
    private lateinit var fieldEmailTrabajador : TextField
    @FXML
    private lateinit var textfieldIdTrabajador : TextField

    @FXML
    private lateinit var fieldIDInforme : TextField
    @FXML
    private lateinit var fieldFranado : TextField
    @FXML
    private lateinit var fieldContaminacion : TextField
    @FXML
    private lateinit var selectorTrabajador : ComboBox<String>
    @FXML
    private lateinit var fieldMatricula : TextField

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
    private lateinit var fieldDNIInforme : TextField
    @FXML
    private lateinit var fieldMatriculaVehiculo : TextField
    @FXML
    private lateinit var fieldMarca : TextField
    @FXML
    private lateinit var fieldModelo : TextField
    @FXML
    private lateinit var fieldFechaMatriculacion : DatePicker
    @FXML
    private lateinit var fieldFechaUltimaRevision : DatePicker
    @FXML
    private lateinit var selectorMotor : ComboBox<String>
    @FXML
    private lateinit var selectorTipoVehiculo : ComboBox<String>
    @FXML
    private lateinit var fieldDNIPropietarioVehiculo : TextField

    @FXML
    private lateinit var  btnNuevo: Button
    @FXML
    private lateinit var btnEditar : Button
    @FXML
    private lateinit var btnEliminar : Button

    @FXML
    fun initialize() {

        iniciarBindings()

        iniciarEventos()

    }

    val estiloEnable = "-fx-opacity: 1"

    private fun iniciarBindings() {
        fieldDNIPropietario.textProperty().bind(mainViewModel.state.dniPropietario)
        fieldNombrePropietario.textProperty().bind(mainViewModel.state.nombrePropietario)
        fieldApellidosPropietario.textProperty().bind(mainViewModel.state.apellidosPropietario)
        fieldTelefonoPropietario.textProperty().bind(mainViewModel.state.telefonoPropietario)
        fieldEmailPropietario.textProperty().bind(mainViewModel.state.emailPropietario)
        textfieldIdTrabajador.textProperty().bind(mainViewModel.state.idTrabajador)
        fieldNombreTrabajador.textProperty().bind(mainViewModel.state.nombreTrabajador)
        fieldEmailTrabajador.textProperty().bind(mainViewModel.state.emailTrabajador)
        fieldIDInforme.textProperty().bind(mainViewModel.state.idInforme)
        fieldDNIInforme.textProperty().bind(mainViewModel.state.dniInforme)
        fieldFranado.textProperty().bind(mainViewModel.state.frenadoInforme)
        fieldContaminacion.textProperty().bind(mainViewModel.state.contaminacionInforme)
        fieldMatricula.textProperty().bind(mainViewModel.state.matriculaInforme)
        fieldDNIPropietario.textProperty().bind(mainViewModel.state.dniPropietario)
        fieldMatriculaVehiculo.textProperty().bind(mainViewModel.state.matriculaVehiculo)
        fieldMarca.textProperty().bind(mainViewModel.state.marcaVehiculo)
        fieldModelo.textProperty().bind(mainViewModel.state.modeloVehiculo)
        fieldDNIPropietarioVehiculo.textProperty().bind(mainViewModel.state.dniInforme)
        selectorHora.valueProperty().bind(mainViewModel.state.horaCita)
        fieldFechaCita.valueProperty().bind(mainViewModel.state.fechaCita)
        fieldFechaMatriculacion.valueProperty().bind(mainViewModel.state.fechaMatriculacion)
        fieldFechaUltimaRevision.valueProperty().bind(mainViewModel.state.ultimaRevision)
        checkboxApto.selectedProperty().bind(mainViewModel.state.apto)
        checkboxLuces.selectedProperty().bind(mainViewModel.state.lucesInforme)
        checkboxInterior.selectedProperty().bind(mainViewModel.state.interiorInforme)

    }

    private fun iniciarEventos() {
        tableInformes.items = mainViewModel.listaInformesDto
        tableColumnDNI.cellValueFactory = PropertyValueFactory("dni")
        tableColumNombre.cellValueFactory = PropertyValueFactory("nombre")
        tableColumnApellidos.cellValueFactory = PropertyValueFactory("apellidos")
        tableColumnTelefono.cellValueFactory = PropertyValueFactory("telefono")
        tableColumnIDTrabajador.cellValueFactory = PropertyValueFactory("idTrabajador")
        tableColumnMatricula.cellValueFactory = PropertyValueFactory("matricula")
        tableColumnTipoMotor.cellValueFactory = PropertyValueFactory("tipoMotor")
        tableColumnTipoVehiculo.cellValueFactory = PropertyValueFactory("tipoVehiculo")
        tableColumnResultado.cellValueFactory = PropertyValueFactory("apto")

        buscadorTipoVehiculo.items = mainViewModel.tiposVehiculos
        buscadorMotor.items = mainViewModel.tiposMotor

        selectorHora.items = mainViewModel.listaHoras
        buscadorNombre.setOnKeyReleased {
            it?.let { onFiltrar() }
        }



        buscadorMotor.selectionModel.selectedIndexProperty().addListener {_,_, newValues ->
            newValues?.let { onFiltrar() }

        }
        buscadorTipoVehiculo.selectionModel.selectedIndexProperty().addListener {_,_, newValues ->
            newValues?.let { onFiltrar() }

        }

        tableInformes.selectionModel.selectedItemProperty().addListener {_,_,newValues ->
            newValues?.let { onSeleccionarTabla(it) }
            btnEditar.isDisable = false
            btnEliminar.isDisable = false
        }

        btnEliminar.setOnAction { eliminarInforme() }

    }

    private fun eliminarInforme() {
        val alerta = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = ""
        }

    }


    private fun onSeleccionarTabla(informe: InformeDto) {
        mainViewModel.seleccionarInforme(informe)
        cambiarSelectores(informe)
        cambiarEstilos()
    }

    private fun cambiarSelectores(informe: InformeDto) {
        selectorTrabajador.value = informe.idTrabajador + " -- " + informe.nombreTrabajador
        selectorMotor.value = informe.tipoMotor
        selectorTipoVehiculo.value = informe.tipoVehiculo
    }

    private fun cambiarEstilos() {
        selectorMotor.style = estiloEnable
        selectorTipoVehiculo.style = estiloEnable
        selectorHora.style = estiloEnable
        fieldFechaCita.style = estiloEnable
        fieldFechaMatriculacion.style = estiloEnable
        fieldFechaUltimaRevision.style = estiloEnable
        selectorTrabajador.style = estiloEnable
        fieldIDInforme.style = estiloEnable
        checkboxApto.style = estiloEnable
        checkboxInterior.style = estiloEnable
        checkboxLuces.style = estiloEnable
    }


    private fun onFiltrar() {
        tableInformes.items = mainViewModel.listaFiltrada(buscadorNombre.text, buscadorMotor.value,
            buscadorTipoVehiculo.value)
    }

}