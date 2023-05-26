package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Ok
import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.util.*

class JsonInformesStorageTest : KoinTest {

    private val logger = KotlinLogging.logger {  }


    val appConfig: AppConfig by inject()
    val databaseManager: DatabaseManager by inject()
    lateinit var database: Connection
    val utilsForTest = UtilsForTest()
    val repositorioInforme : InformeRepositoryImpl by inject()
    val jsonInformesStorage : JsonInformesStorage by inject()

    @BeforeEach
    fun iniciarTest() {


        cambiarValores()

        databaseManager.dropAllTables(true)
        databaseManager.createTables()
        database = databaseManager.bd
        utilsForTest.initValoresBd(database)

    }

    @AfterEach
    fun borrarTest() {

    }

    private fun cambiarValores() {
        val properties = Properties()

        val fileInputStream = FileInputStream("src/test/resources/config.properties")
        properties.load(fileInputStream)

        appConfig.loadProperties()

        appConfig.bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        appConfig.dataPath = properties.getProperty("data.path") ?: "data"

        appConfig.bdName = properties.getProperty("bd.name") ?: "bbitv"

        appConfig.initDataFolder()


    }

    @Test
    fun jsonInformeTest() {
        val fileName = System.getProperty("user.dir") + File.separator + appConfig.dataPath + File.separator + "FicheroTest.json"
        val listaGuardar = repositorioInforme.loadAll().component1()!!

        jsonInformesStorage.saveFile(url = fileName, list = listaGuardar)

        val res = jsonInformesStorage.loadFile(fileName)

        assertEquals(Ok(listaGuardar), res)
    }

    @Test
    fun jsonSaveNotUrlTest() {
        val fileName = "" + File.separator + "agaghah"
        val listaGuardar = repositorioInforme.loadAll().component1()!!.also {
            println(it.size)
        }

        val res = jsonInformesStorage.saveFile(url = fileName, list = listaGuardar)

        assertTrue { res.component2() is StorageErrors.JsonStorageError }
    }

    @Test
    fun jsonLoadNotUrlTest() {
        val fileName = "" + File.separator + "agaghah"

        val res = jsonInformesStorage.loadFile(url = fileName)

        assertTrue { res.component2() is StorageErrors.JsonStorageError }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun startup() {
            startKoin {
                this.modules(Modulo)
            }
        }

        @JvmStatic
        @AfterAll
        fun cerrarKoin () {
            stopKoin()
        }
    }
}