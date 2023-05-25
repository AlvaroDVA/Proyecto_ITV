package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.errors.TrabajadorErrors
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.FileInputStream
import java.sql.Connection
import java.util.*

class TrabajadorRepositoryImplTest () : KoinComponent{

    private val logger = KotlinLogging.logger {  }

    val appConfig : AppConfig by inject()
    val databaseManager : DatabaseManager by inject()
    lateinit var database : Connection
    val utilsForTest = UtilsForTest()

    @BeforeEach
    fun iniciarTest() {
        cambiarValores()

        databaseManager.dropAllTables(true)
        databaseManager.createTables()
        database = databaseManager.bd
        utilsForTest.initValoresBd(database)

    }

    private fun cambiarValores() {
        val properties = Properties()

        val fileInputStream = FileInputStream("src/test/resources/config.properties")
        properties.load(fileInputStream)

        appConfig.bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        appConfig.dataPath = properties.getProperty("data.path") ?: "data"

        appConfig.bdName = properties.getProperty("bd.name") ?: "bbitv"
    }

    val repositorioTrabajador : TrabajadorRepositoryImpl = TrabajadorRepositoryImpl()

    val trabajadores = repositorioTrabajador.loadAll().component1()!!

    @Test
    fun loadAllTest() {
        val res = repositorioTrabajador.loadAll()

        assertTrue(trabajadores[0] == res.component1()!![0])
        assertTrue(trabajadores[1] == res.component1()!![1])
        assertTrue(trabajadores[2] == res.component1()!![2])
        assertTrue(trabajadores[3] == res.component1()!![3])
        assertTrue(trabajadores[4] == res.component1()!![4])
    }

    @Test
    fun findByIdTest() {
        val res1 = repositorioTrabajador.findById(3)
        val res2 = repositorioTrabajador.findById(5)
        val res3 = repositorioTrabajador.findById(6)
        val res4 = repositorioTrabajador.findById(-1)

        assertTrue(trabajadores[2] == res1.component1())
        assertTrue(trabajadores[4] == res2.component1())
        assertTrue(res3.component2() is TrabajadorErrors.TrabajadorNotFoundError)
        assertTrue(res4.component2() is TrabajadorErrors.TrabajadorNotFoundError)
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

