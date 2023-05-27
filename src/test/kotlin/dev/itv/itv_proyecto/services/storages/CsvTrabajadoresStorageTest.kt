package dev.itv.itv_proyecto.services.storages

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.util.*

class CsvTrabajadoresStorageTest : KoinTest {

    private val logger = KotlinLogging.logger {  }

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()
    val appConfig : AppConfig by inject()

    lateinit var trabajadoresRepository : TrabajadorRepositoryImpl
    val csvTrabajadoresStorage = CsvTrabajadoresStorage()

    val trabajadores = mutableListOf<Trabajador>()

    @BeforeEach
    fun iniciarTest() {

        cambiarValores()
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        trabajadoresRepository = TrabajadorRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        trabajadores.addAll(
            listOf(
                Trabajador(
                    nombreTrabajador = "Juan Pérez",
                    telefonoTrabajador = 123456789,
                    email = "juan@example.com",
                    username = "juancito",
                    password = "password123",
                    fechaContratacion = LocalDate.parse("2017-12-01"),
                    especialidad = Especialidad.MOTOR,
                    isResponsable = false
                ),
                Trabajador(
                    nombreTrabajador = "María López",
                    telefonoTrabajador = 987654321,
                    email = "maria@example.com",
                    username = "marialo",
                    password = "password456",
                    fechaContratacion = LocalDate.parse("2022-08-12"),
                    especialidad = Especialidad.ADMINISTRACION,
                    isResponsable = true
                ),
                Trabajador(
                    nombreTrabajador = "Pedro Rodríguez",
                    telefonoTrabajador = 555555555,
                    email = "pedro@example.com",
                    username = "pedror",
                    password = "password789",
                    fechaContratacion = LocalDate.parse("2021-05-10"),
                    especialidad = Especialidad.ELECTRICIDAD,
                    isResponsable = false
                )
            )
        )


    }

    private fun cambiarValores() {
        val properties = Properties()

        val fileInputStream = FileInputStream("src/test/resources/config.properties")
        properties.load(fileInputStream)

        appConfig.loadProperties()

        appConfig.bdPath = properties.getProperty("bd.path") ?: "127.0.0.1"
        appConfig.dataPath = properties.getProperty("data.path") ?: "data"

        appConfig.bdName = properties.getProperty("bd.name") ?: "Testbbitv"

        appConfig.initDataFolder()

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
    }

    @Test
    fun csvTrabajadorTest() {
        val fileName = System.getProperty("user.dir") + File.separator + appConfig.dataPath + File.separator + "FicheroTest.csv"

        csvTrabajadoresStorage.saveFile(trabajadores, fileName)

        val res = csvTrabajadoresStorage.loadFile(fileName)

        assertEquals(trabajadores, res.component1())

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