package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.errors.TrabajadorErrors
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.sql.DriverManager

class TrabajadorRepositoryImplTest () : KoinComponent{

    private val logger = KotlinLogging.logger {  }

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()

    lateinit var repositorioTrabajador : TrabajadorRepositoryImpl

    val trabajadores = mutableListOf<Trabajador>()

    @BeforeEach
    fun iniciarTest() {
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        utilsForTest.dropAllTables(database, true)
        utilsForTest.createTables(database)

        utilsForTest.initValoresBd(database)

        repositorioTrabajador = TrabajadorRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        trabajadores.addAll(repositorioTrabajador.loadAll().component1()!!)

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
    }

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

