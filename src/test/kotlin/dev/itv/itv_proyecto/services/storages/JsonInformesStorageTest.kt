package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Ok
import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class JsonInformesStorageTest : KoinTest {

    private val logger = KotlinLogging.logger {  }

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()
    val appConfig : AppConfig by inject()

    lateinit var informesRepository : InformeRepositoryImpl
    val jsonInformesStorage = JsonInformeStorage()

    val informes = mutableListOf<Informe>()

    @BeforeEach
    fun iniciarTest() {

        cambiarValores()
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        utilsForTest.dropAllTables(database, true)
        utilsForTest.createTables(database)

        utilsForTest.initValoresBd(database
        )
        informesRepository = InformeRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        informes.addAll(informesRepository.loadAll().component1()!!)

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
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
        val listaGuardar = informesRepository.loadAll().component1()!!

        jsonInformesStorage.saveFile(url = fileName, informe = listaGuardar[1])


        assertTrue(File(fileName).exists())
    }

    @Test
    fun jsonSaveNotUrlTest() {
        val fileName = "" + File.separator + "agaghah"
        val listaGuardar = informesRepository.loadAll().component1()!!.also {
            println(it.size)
        }

        val res = jsonInformesStorage.saveFile(url = fileName, informe = listaGuardar[1])

        assertTrue { res.component2() is StorageErrors.JsonStorageError }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun startup() {
            startKoin {
                this.modules(moduloTest)
            }
        }

        @JvmStatic
        @AfterAll
        fun cerrarKoin () {
            stopKoin()
        }
    }
}