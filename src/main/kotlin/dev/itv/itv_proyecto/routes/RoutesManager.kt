package dev.itv.itv_proyecto.routes

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.enums.ActionView
import dev.itv.itv_proyecto.enums.Icons
import dev.itv.itv_proyecto.enums.Views
import dev.itv.itv_proyecto.models.states.MainState
import dev.itv.itv_proyecto.services.database.DatabaseManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream
import java.net.URL

private val logger = KotlinLogging.logger {  }
object RoutesManager : KoinComponent {

    lateinit var mainApp : Application
    private val mainWidht = 1000.0
    private val mainHeight = 850.0

    private lateinit var _mainStage : Stage
    val mainStage : Stage get() = _mainStage
    private lateinit var _actualStage : Stage
    val actualStage : Stage get() = _actualStage

    val appConfig : AppConfig by inject()
    val databaseManager : DatabaseManager by inject()

    init {
        databaseManager.bd
    }

    /**
     * Inciar Aplicación
     */
    fun iniciarEscenario(stage : Stage) {

        logger.debug { " Iniciando Escenario " }
        val fxmlLoader = FXMLLoader(getResource(Views.MAIN.url))
        val loadParent = fxmlLoader.load<Pane>()
        stage.setOnCloseRequest { close() }

        val escena = Scene(loadParent ,mainWidht, mainHeight)
        stage.isResizable = false
        stage.icons.add(Image(getResourceAsStream(Icons.LOGO.url)))
        stage.title = "ITV - DAMLLER"
        stage.scene = escena
        stage.setOnCloseRequest {
            it.consume()
            cerrarApp()
        }

        _mainStage = stage
        _actualStage = _mainStage
        _mainStage.show()

    }

    /**
     * Iniciar Editar Ventana
     */
    fun editarVentana() {
        logger.debug { "Iniciando Segunda Vista " }
        val fxmlLoader = FXMLLoader(getResource(Views.NUEVO.url))
        val loadParent = fxmlLoader.load<Pane>()

        val escena = Scene(loadParent,1000.0, 540.0 )
        val stage = Stage()
        stage.title = "Nuevo / Editar Informe"
        stage.scene = escena
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        _actualStage = stage
        stage.icons.add(Image(getResourceAsStream(Icons.LOGO.url)))
        stage.show()
    }

    /**
     * Iniciar Ventana Acerca
     */
    fun iniciarAcerca() {
        logger.debug { "Iniciando Acerca de"}
        val fxmlLoader = FXMLLoader(getResource(Views.ACERCA.url))
        val loadParent = fxmlLoader.load<Pane>()

        val escena = Scene(loadParent, 500.0, 300.0)
        val stage = Stage()
        stage.title = "Acerca De Damller"
        stage.scene = escena
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.icons.add(Image(getResourceAsStream(Icons.LOGO.url)))
        stage.show()
    }

    var compartirState = MainState()
    var action = ActionView.NEW


    fun close() {
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = ""
            initOwner(mainStage)

            initModality(Modality.WINDOW_MODAL)
        }
        _mainStage.close()
    }

    fun getResource(recurso : String) : URL? {
        return mainApp::class.java.getResource(recurso)
            ?: return null
    }

    fun getResourceAsStream (recurso : String) : InputStream? {
        return mainApp::class.java.getResourceAsStream(recurso)
            ?: return null
    }

    fun cerrarApp() {
        val alert = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Confirmación"
            headerText = ""
            contentText = "¿Seguro que quieres salir?"
            buttonTypes.setAll(ButtonType.OK, ButtonType.CANCEL)
        }

        val result = alert.showAndWait()
        if (result.isPresent && result.get() == ButtonType.OK) {
            // Acción a realizar al presionar "Aceptar"
            _mainStage.close()
        }
    }

    fun cerrarEditar() {
        _actualStage.close()
        _actualStage = _mainStage
    }




}