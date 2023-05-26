package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.services.storages.JsonInformesStorage
import org.koin.dsl.module
import java.io.FileInputStream
import java.util.*

val Modulo = module {
    single { AppConfig() }
    single { DatabaseManager() }
    single { RoutesManager() }
    factory<TrabajadorRepositoryImpl> { TrabajadorRepositoryImpl() }
    factory { JsonInformesStorage() }
    single { createConfigProperties() }
}

class ConfigProperties(private val properties: Properties) {
    val dataPath: String by lazy { properties.getProperty("data.path", "data") }
    val bdPath: String by lazy { properties.getProperty("bd.path", "127.0.0.1") }
    val bdName: String by lazy { properties.getProperty("bd.name", "bbitv") }
}

private fun createConfigProperties(): ConfigProperties {
    val properties = Properties()
    val configFile = "config.properties" // Nombre del archivo de propiedades
    val inputStream = FileInputStream(configFile)
    properties.load(inputStream)
    return ConfigProperties(properties)
}