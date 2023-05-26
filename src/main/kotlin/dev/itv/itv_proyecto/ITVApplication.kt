package dev.itv.itv_proyecto

import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

private val logger = KotlinLogging.logger {  }
class ITVApplication : Application() , KoinComponent{
    override fun start(stage: Stage) {
        logger.debug { "Iniciando Programa ITV" }
        startKoin {
            logger.debug { " Cargando Modulo Koin " }
            modules(Modulo)
        }

        val routesManager : RoutesManager by inject()

        routesManager.apply {
            mainApp = this@ITVApplication
        }
        routesManager.iniciarEscenario(stage)
    }
}

fun main() {
    Application.launch(ITVApplication::class.java)
}