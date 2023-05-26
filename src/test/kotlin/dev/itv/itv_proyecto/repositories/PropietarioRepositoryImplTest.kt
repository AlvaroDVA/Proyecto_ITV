package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.sql.DriverManager
class PropietarioRepositoryImplTest : KoinComponent {

    private val logger = KotlinLogging.logger {  }

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()

    lateinit var repositorioProtetario : PropietarioRepositoryImpl

    val propietarios = mutableListOf<Propietario>()

    @BeforeEach
    fun iniciarTest() {
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        utilsForTest.dropAllTables(database, true)
        utilsForTest.createTables(database)

        utilsForTest.initValoresBd(database
        )
        repositorioProtetario = PropietarioRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        propietarios.addAll(repositorioProtetario.loadAll().component1()!!)

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
    }

    @Test
    fun loadAllTest() {
        val res = repositorioProtetario.loadAll()

        assertTrue(propietarios[0] == res.component1()!![0])
        assertTrue(propietarios[1] == res.component1()!![1])
        assertTrue(propietarios[2] == res.component1()!![2])
        assertTrue(propietarios[3] == res.component1()!![3])
        assertTrue(propietarios[4] == res.component1()!![4])
        assertThrows<IndexOutOfBoundsException> { res.component1()!![5] }
    }

    @Test
    fun findByIdTest() {

        val res1 = repositorioProtetario.findById(propietarios[0].dni)
        val res2 = repositorioProtetario.findById(propietarios[4].dni)
        val res3 = repositorioProtetario.findById("AAAA222")

        assertTrue(propietarios[0] == res1.component1())
        assertTrue(propietarios[4] == res2.component1())
        assertTrue(res3.component2() is PropietarioErrors.PropietarioNotFoundError )

    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun startup() {
            startKoin {
                modules(moduloTest)
            }
        }


        @AfterAll
        @JvmStatic
        fun cerrarKoin() {
            stopKoin()
        }

    }
}
