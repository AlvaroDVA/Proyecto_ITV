package dev.itv.itv_proyecto.controllers

import dev.itv.itv_proyecto.enums.ActionExportar
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

private val logger = KotlinLogging.logger {  }
class IndexController : KoinComponent{

    val mainViewModel : MainViewModel by inject()
    @FXML
    private lateinit var menuExportarJSON : MenuItem
    @FXML
    private lateinit var menuExportarHTML : MenuItem
    @FXML
    private lateinit var menuExportarCsv : MenuItem
    @FXML
    private lateinit var menuSalir : MenuItem
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
    private lateinit var tableColumniD : TableColumn<InformeDto, String>
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

    /**
     * Funcion que inicia los Binding. Son unidirectional porque los en esta vista no queremos que nunca modifiquen él State
     */
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

        selectorMotor.valueProperty().bind(mainViewModel.state.tipoMotor)
        selectorTipoVehiculo.valueProperty().bind(mainViewModel.state.tipoVehiculo)
        selectorTrabajador.valueProperty().bind(mainViewModel.state.trabajadorInforme)


    }

    /**
     * Función que asigna las acciones de los elementos de la Interfaz Grafica
     */
    private fun iniciarEventos() {
        tableInformes.items = mainViewModel.listaInformesDto
        tableColumniD.cellValueFactory = PropertyValueFactory("idInforme")
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
            newValues?:let{
                btnEditar.isDisable = true
                btnEliminar.isDisable = true
            }
        }


        btnEliminar.setOnAction { onEliminarInforme() }

        btnEditar.setOnAction { onBotonEditar() }

        btnNuevo.setOnAction { onBotonNuevo() }

        menuSalir.setOnAction { onBotonSalir() }

        menuExportarHTML.setOnAction { onBotonHtml() }
        menuExportarCsv.setOnAction { onBotonCsv() }
        menuExportarJSON.setOnAction { onBotonJson() }

        menuAcercaDe.setOnAction { onBotonAcerca() }
    }


    /**
     * Función que cree un Alert de Confirmación para eliminar un Informe. Si se pulsa el botón de confirmar se borrara el informe
     *
     * @see MainViewModel.eliminarInforme
     */
    private fun onEliminarInforme() {
        val alert = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Confirmación"
            headerText = ""
            contentText = "Esta acción no se puede deshacer."
            buttonTypes.setAll(ButtonType.OK, ButtonType.CANCEL)
        }

        val result = alert.showAndWait()
        if (result.isPresent && result.get() == ButtonType.OK) {
            // Acción a realizar al presionar "Aceptar"
            mainViewModel.eliminarInforme()
        }

    }

    /**
     * Función que pasa los datos a la lógica para usarse. También aplica estilos a partes para hacerla mas visible
     *
     * @see MainViewModel.seleccionarInforme
     * @see cambiarEstilos
     */

    private fun onSeleccionarTabla(informe: InformeDto) {
        mainViewModel.seleccionarInforme(informe)
        cambiarEstilos()
    }

    /**
     * Funcion que cambia los estilos de partes deshabilitadas para mejorar su lectura
     */
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

    /**
     * Funcion que recoge los datos de filtrado de la tabla
     *
     * @see MainViewModel.listaFiltrada
     */
    private fun onFiltrar() {
        tableInformes.items = mainViewModel.listaFiltrada(buscadorNombre.text, buscadorMotor.value,
            buscadorTipoVehiculo.value)
    }

    /**
     * Función que abre la pestaña de Editar / Nuevo con campos vacios
     *
     * @see MainViewModel.cambiarVentana
     */
    private fun onBotonNuevo() {
        mainViewModel.cambiarVentana(ActionView.NEW)

    }

    /**
     * Función que abre la pestaña de Editar / Nuevo con campos del state
     *
     * @see MainViewModel.cambiarVentana
     */
    private fun onBotonEditar()  {
        mainViewModel.cambiarVentana(ActionView.UPDATE)
    }

    /**
     * Función que cierra la Aplicación
     *
     * @see RoutesManager.cerrarApp
     */
    private fun onBotonSalir() {
        RoutesManager.cerrarApp()
    }

    /**
     * Función que abre un File Chooser para exportar a HTML
     *
     * @seleccionarLugar
     */
    private fun onBotonHtml() {
        seleccionarLugar(ActionExportar.EXPORTAR_HTML)
    }

    /**
     * Función que abre un File Chooser para exportar a JSON
     *
     * @see seleccionarLugar
     */
    private fun onBotonJson() {
        seleccionarLugar(ActionExportar.EXPORTAR_JSON)
    }

    /**
     * Función que abre un File Chooser para exportar a CSV
     *
     * @see seleccionarLugar
     */
    private fun onBotonCsv() {
        seleccionarLugar(ActionExportar.EXPORTAR_CSV)
    }

    /**
     * Función que abre la ventana de acerca de
     *
     * @see RoutesManager.iniciarAcerca
     */
    private fun onBotonAcerca () {
        RoutesManager.iniciarAcerca()
    }

    /**
     * Funcion que abré un File Chooser para guardar él archivó dependiendo de la accion seleccionada
     *
     * @param accion Tipo de Exportación
     */
    fun seleccionarLugar(accion: ActionExportar) {
        when (accion) {
            ActionExportar.EXPORTAR_HTML -> {
                FileChooser().run {
                    title = "Exportar HTML"
                    initialDirectory = File("data/")
                    extensionFilters.add(FileChooser.ExtensionFilter("HTML", ".html"))
                    showSaveDialog(RoutesManager.mainStage)
                }?.let {
                    mainViewModel.guardarArchivo(it.absolutePath , accion)
                }
            }
            ActionExportar.EXPORTAR_CSV -> {
                FileChooser().run {
                    title = "Exportar CSV"
                    initialDirectory = File("data/")
                    extensionFilters.add(FileChooser.ExtensionFilter("CSV", ".csv"))
                    showSaveDialog(RoutesManager.mainStage)
                }?.let {
                    mainViewModel.guardarArchivo(it.absolutePath , accion)
                }
            }
            ActionExportar.EXPORTAR_JSON -> {
                FileChooser().run {
                    title = "Exportar JSON"
                    initialDirectory = File("data")
                    extensionFilters.add(FileChooser.ExtensionFilter("JSON", ".json"))
                    showSaveDialog(RoutesManager.mainStage)
                }?.let {
                    mainViewModel.guardarArchivo(it.absolutePath , accion)
                }
            }
        }
    }

}