package dev.itv.itv_proyecto

import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.context.startKoin

private val logger = KotlinLogging.logger {  }
class ITVApplication : Application() {
    override fun start(stage: Stage) {
        logger.debug { "Iniciando Programa ITV" }
        startKoin {
            logger.debug { " Cargando Modulo Koin " }
            modules(Modulo)
        }

        RoutesManager.apply {
            mainApp = this@ITVApplication
        }
        RoutesManager.iniciarEscenario(stage)
    }
}

fun main() {
    Application.launch(ITVApplication::class.java)
}