package dev.itv.itv_proyecto.routes

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.enums.Icons
import dev.itv.itv_proyecto.enums.Views
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.io.InputStream
import java.net.URL

private val logger = KotlinLogging.logger {  }
object RoutesManager : KoinComponent {

    lateinit var mainApp : Application
    private val mainWidht = 884.0
    private val mainHeight = 636.0

    private lateinit var _mainStage : Stage
    val mainStage : Stage get() = _mainStage
    private lateinit var _actualStage : Stage
    val actualStage : Stage get() = _actualStage

    val appConfig = AppConfig()

    fun iniciarEscenario(stage : Stage) {

        appConfig.dataPath

        logger.debug { " Iniciando Escenario " }
        val fxmlLoader = FXMLLoader(getResource(Views.MAIN.url))
        val loadParent = fxmlLoader.load<Pane>()

        val escena = Scene(loadParent ,mainWidht, mainHeight)
        stage.isResizable = false
        stage.icons.add(Image(getResourceAsStream(Icons.LOGO.url)))
        stage.title = "ITV - DAMLLER"
        stage.scene = escena

        _mainStage = stage
        _actualStage = _mainStage
        _mainStage.show()

    }


    fun close() {
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
}