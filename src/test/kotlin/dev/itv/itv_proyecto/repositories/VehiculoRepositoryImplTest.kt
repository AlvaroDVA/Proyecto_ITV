package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.utils.UtilsForTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate

class VehiculoRepositoryImplTest : KoinComponent {

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()

    lateinit var repositorioVehiculos : VehiculoRepositoryImpl

    val vehiculos = mutableListOf<Vehiculo>()

    @BeforeEach
    fun iniciarTest() {
        database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")

        utilsForTest.dropAllTables(database, true)
        utilsForTest.createTables(database)

        utilsForTest.initValoresBd(database)

        repositorioVehiculos = VehiculoRepositoryImpl().apply {
            database = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/TestbbITV", "root", "")
        }

        vehiculos.addAll(repositorioVehiculos.loadAll().component1()!!)

    }

    @AfterEach
    fun closeBaseDatos () {
        database.close()
    }

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

    @Test
    fun saveTest() {

        val propietario = Propietario(
            dni = "777777777E",
            nombre = "Propietario 5",
            apellidos = "Apellidos 5",
            telefono = 777777777,
            emailPropietario = "propietario5@example.com"
        )

        val vehiculo = Vehiculo(
            matricula = "ABCKI123",
            marca = "Ford",
            modelo = "Focus",
            fechaMatricula = LocalDate.of(2020, 5, 15),
            fechaUltimaRevision = LocalDate.of(2022, 8, 20),
            tipoMotor = TipoMotor.GASOLINA,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietario
        )

        val res1 = repositorioVehiculos.loadAll()
        assertEquals(5, res1.component1()!!.size)

        val res2 = repositorioVehiculos.save(vehiculo)
        val res3 = repositorioVehiculos.loadAll()

        assertEquals(6, res3.component1()!!.size)
        assertEquals(res2.component1(), vehiculo)

    }

    @Test
    fun saveVehiculoExistente() {

        val propietario = Propietario(
            dni = "777777777E",
            nombre = "Propietario 5",
            apellidos = "Apellidos 5",
            telefono = 777777777,
            emailPropietario = "propietario5@example.com"
        )

        val vehiculo = Vehiculo(
            matricula = "QWE456",
            marca = "Ford",
            modelo = "Focus",
            fechaMatricula = LocalDate.of(2020, 5, 15),
            fechaUltimaRevision = LocalDate.of(2022, 8, 20),
            tipoMotor = TipoMotor.GASOLINA,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietario
        )

        val res = repositorioVehiculos.save(vehiculo)

        assertTrue(res.component2() is VehiculosErrors.VehiculoFoundError)

    }

    @Test
    fun savePropietarioNoExiste() {
        val propietario = Propietario(
            dni = "777777777E",
            nombre = "Propietario 5",
            apellidos = "Apellidos 5",
            telefono = 777777777,
            emailPropietario = "propietario5@example.com"
        )

        val vehiculo = Vehiculo(
            matricula = "ZXC321",
            marca = "Ford",
            modelo = "Focus",
            fechaMatricula = LocalDate.of(2020, 5, 15),
            fechaUltimaRevision = LocalDate.of(2022, 8, 20),
            tipoMotor = TipoMotor.GASOLINA,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietario
        )

        val res = repositorioVehiculos.save(vehiculo)

        assertTrue(res.component2() is VehiculosErrors.VehiculoFoundError)
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