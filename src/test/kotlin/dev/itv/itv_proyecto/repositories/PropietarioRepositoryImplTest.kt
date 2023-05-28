package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.errors.PropietarioErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
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

    lateinit var repositorioPropetario : PropietarioRepositoryImpl

    val propietarios = mutableListOf<Propietario>()

    @BeforeEach
    fun iniciarTest() {
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        utilsForTest.dropAllTables(database, true)
        utilsForTest.createTables(database)

        utilsForTest.initValoresBd(database
        )
        repositorioPropetario = PropietarioRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        propietarios.addAll(repositorioPropetario.loadAll().component1()!!)

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
    }

    @Test
    fun loadAllTest() {
        val res = repositorioPropetario.loadAll()

        assertTrue(propietarios[0] == res.component1()!![0])
        assertTrue(propietarios[1] == res.component1()!![1])
        assertTrue(propietarios[2] == res.component1()!![2])
        assertTrue(propietarios[3] == res.component1()!![3])
        assertTrue(propietarios[4] == res.component1()!![4])
        assertThrows<IndexOutOfBoundsException> { res.component1()!![5] }
    }

    @Test
    fun findByIdTest() {

        val res1 = repositorioPropetario.findById(propietarios[0].dni)
        val res2 = repositorioPropetario.findById(propietarios[4].dni)
        val res3 = repositorioPropetario.findById("AAAA222")

        assertTrue(propietarios[0] == res1.component1())
        assertTrue(propietarios[4] == res2.component1())
        assertTrue(res3.component2() is PropietarioErrors.PropietarioNotFoundError )

    }

    @Test
    fun savePropietarioTest() {

        val propietario = Propietario(
            dni = "12345678A",
            nombre = "Juan",
            apellidos = "Pérez",
            telefono = 123456789,
            emailPropietario = "juan.perez@example.com"
        )

        val res1 = repositorioPropetario.loadAll()

        assertEquals(5, res1.component1()!!.size)

        val res2 = repositorioPropetario.savePropietario(propietario)
        val res3 = repositorioPropetario.loadAll()
        assertEquals(6, res3.component1()!!.size)
        assertEquals(res2.component1(), propietario)

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
