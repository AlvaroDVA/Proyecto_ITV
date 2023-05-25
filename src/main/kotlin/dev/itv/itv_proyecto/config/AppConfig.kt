package dev.itv.itv_proyecto.config

import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class AppConfig {
    lateinit var dataPath : String
    lateinit var bdPath : String
    lateinit var bdName : String

    private val logger = KotlinLogging.logger {}

    init {
        loadProperties()
        initDataFolder()
    }

    fun loadProperties() {
        logger.debug { " AppConfig -- LoadProperties() " }
        val properties = Properties()

        val fileInputStream = FileInputStream("src/main/resources/config.properties")
        properties.load(fileInputStream)

        bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        dataPath = properties.getProperty("data.path") ?: "data"

        bdName = properties.getProperty("bd.name") ?: "bbitv"

    }


    private fun initDataFolder() {
        logger.debug { " Creando $dataPath si no existe " }
        Files.createDirectories(Paths.get(dataPath))
    }


}