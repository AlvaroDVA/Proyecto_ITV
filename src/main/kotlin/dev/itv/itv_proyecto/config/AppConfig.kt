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

    private val logger = KotlinLogging.logger {}

    private val localPath = "${System.getProperty("user.dir")}${File.separator}"

    init {
        loadProperties()
        initDataFolder()
    }

    private fun loadProperties() {
        logger.debug { " AppConfig -- LoadProperties() " }
        val properties = Properties()

        val configFile = "config.properties"

        logger.debug { configFile }

        val fileInputStream = FileInputStream("src/main/resources/config.properties")
        properties.load(fileInputStream)

        bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        dataPath = properties.getProperty("data.path") ?: "data"

        dataPath = localPath + dataPath

    }


    private fun initDataFolder() {
        logger.debug { " Creando $dataPath si no existe " }
        Files.createDirectories(Paths.get(dataPath))
    }


}