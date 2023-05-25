package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.Modulo
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.FileInputStream
import java.sql.Connection
import java.util.*

class PropietarioRepositoryImplTest : KoinComponent {

    private val logger = KotlinLogging.logger {  }

    val appConfig: AppConfig by inject()
    val databaseManager: DatabaseManager by inject()
    lateinit var database: Connection
    val utilsForTest = UtilsForTest()

    @BeforeEach
    fun iniciarTest() {

        cambiarValores()

        databaseManager.dropAllTables(true)
        databaseManager.createTables()
        database = databaseManager.bd
        utilsForTest.initValoresBd(database)

    }

    val propietariosRepository = PropietarioRepositoryImpl()

    val propietarios = propietariosRepository.loadAll().component1()!!

    private fun cambiarValores() {
        val properties = Properties()

        val fileInputStream = FileInputStream("src/test/resources/config.properties")
        properties.load(fileInputStream)

        appConfig.bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        appConfig.dataPath = properties.getProperty("data.path") ?: "data"

        appConfig.bdName = properties.getProperty("bd.name") ?: "bbitv"
    }


    @Test
    fun loadAllTest() {
        val res = propietariosRepository.loadAll()

        assertTrue(propietarios[0] == res.component1()!![0])
        assertTrue(propietarios[1] == res.component1()!![1])
        assertTrue(propietarios[2] == res.component1()!![2])
        assertTrue(propietarios[3] == res.component1()!![3])
        assertTrue(propietarios[4] == res.component1()!![4])
        assertThrows<IndexOutOfBoundsException> { res.component1()!![5] }
    }

    @Test
    fun findByIdTest() {

        val res1 = propietariosRepository.findById(propietarios[0].dni)
        val res2 = propietariosRepository.findById(propietarios[4].dni)
        val res3 = propietariosRepository.findById("AAAA222")

        assertTrue(propietarios[0] == res1.component1())
        assertTrue(propietarios[4] == res2.component1())
        assertTrue(res3.component2() is PropietarioErrors.PropietarioNotFoundError )

    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun startup() {
            startKoin {
                modules(Modulo)
            }
        }

        @AfterAll
        @JvmStatic
        fun cerrarKoin() {
            stopKoin()
        }
    }
}
