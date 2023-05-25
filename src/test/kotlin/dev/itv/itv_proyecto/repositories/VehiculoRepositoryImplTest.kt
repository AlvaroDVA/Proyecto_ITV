package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.FileInputStream
import java.sql.Connection
import java.util.*

class VehiculoRepositoryImplTest : KoinComponent {

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

    val repositorioVehiculos = VehiculoRepositoryImpl()

    private val vehiculos = repositorioVehiculos.loadAll().component1()!!

    @Test
    fun loadAllTest() {
        val res = repositorioVehiculos.loadAll()

        assertTrue(vehiculos[0] == res.component1()!![0])
        assertTrue(vehiculos[1] == res.component1()!![1])
        assertTrue(vehiculos[2] == res.component1()!![2])
        assertTrue(vehiculos[3] == res.component1()!![3])
        assertTrue(vehiculos[4] == res.component1()!![4])
    }

    @Test
    fun findByIdTest() {
        val res1 = repositorioVehiculos.findById(vehiculos[0].matricula)
        val res2 = repositorioVehiculos.findById(vehiculos[4].matricula)
        val res3 = repositorioVehiculos.findById("EREEEEE")

        assertTrue(vehiculos[0] == res1.component1())
        assertTrue(vehiculos[4] == res2.component1())
        assertTrue(res3.component2() is VehiculosErrors.VehiculoNotFoundError)
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