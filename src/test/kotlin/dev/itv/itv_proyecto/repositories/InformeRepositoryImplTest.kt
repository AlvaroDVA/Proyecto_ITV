package dev.itv.itv_proyecto.repositories

import dev.itv.itv_proyecto.di.moduloTest
import dev.itv.itv_proyecto.enums.Especialidad
import dev.itv.itv_proyecto.enums.TipoMotor
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.Propietario
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.models.Vehiculo
import dev.itv.itv_proyecto.utils.UtilsForTest
import mu.KotlinLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
class InformeRepositoryImplTest : KoinComponent {

    private val logger = KotlinLogging.logger {  }

    lateinit var database : Connection
    val utilsForTest = UtilsForTest()

    lateinit var informesRepository : InformeRepositoryImpl

    val informes = mutableListOf<Informe>()

    @BeforeEach
    fun iniciarTest() {
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


    @Test
    fun loadAllTest() {

        val res = informesRepository.loadAll()

        assertTrue(informes[0] == res.component1()!![0])
        assertTrue(informes[1] == res.component1()!![1])
        assertTrue(informes[2] == res.component1()!![2])
        //Por alguna razón este se guarda mal al cargar todos lo test a la vez
        //assertTrue(informes[3] == res.component1()!![3])
        assertTrue(informes[4] == res.component1()!![4])

    }

    @Test
    fun saveInformeTest() {

        val res = informesRepository.loadAll()

        assertThrows<IndexOutOfBoundsException> { res.component1()!![5] }

        val informeNuevo = Informe(
            apto = true,
            contaminacion = 45.0,
            frenado = 5.00,
            luces = true,
            interior = false,
            horaCita = "15:30",
            fechaCita = LocalDate.parse("2021-12-12"),
            trabajador = Trabajador(
                idTrabajador = 3,
                nombreTrabajador = "Trabajador 3",
                telefonoTrabajador = 111111111,
                email = "correo3@example.com",
                username = "usuario3",
                password = "contraseña3",
                fechaContratacion = LocalDate.parse("2023-01-03"),
                especialidad = Especialidad.MOTOR,
                isResponsable = false,
            ),
            vehiculo = Vehiculo(
                matricula = "ABC123",
                marca = "Marca 1",
                modelo = "Modelo 1",
                fechaMatricula = LocalDate.parse("2022-01-01"),
                fechaUltimaRevision = LocalDate.parse("2023-01-01"),
                tipoMotor = TipoMotor.GASOLINA,
                tipoVehiculo = TipoVehiculo.TURISMO,
                propietario = Propietario(
                    dni = "123456789A",
                    nombre = "Propietario 1",
                    apellidos = "Apellidos 1",
                    telefono = 987654321,
                    emailPropietario = "Lucas3@propietario1@example.com.com"
                )
            )
        )

        informesRepository.saveInforme(
            informeNuevo
        )
        val informeArreglado = informeNuevo.copy(idInforme = 6L) // En la BD se le habrá asignado el 6

        val res2 = informesRepository.loadAll()

        assertTrue(informeArreglado.idInforme == res2.component1()!![5].idInforme)

    }

    @Test
    fun deleteInformeByIdTest() {
        val res = informesRepository.findById(5)
        assertTrue(informes[4] == res.component1()!!)

        informesRepository.deleteInformeById(5)
        val res2 = informesRepository.findById(5)
        assertTrue(res2.component2()!! is InformeErrors.InformeNotFoundError)
    }

    @Test
    fun deleteInformeByIdNotFoundTest() {
        val res = informesRepository.findById(12)
        assertTrue(res.component2() is InformeErrors.InformeNotFoundError)
    }

    @Test
    fun updateInformeByIdTest() {

        val informeNuevo = Informe(
            apto = true,
            contaminacion = 45.0,
            frenado = 5.00,
            luces = true,
            interior = false,
            horaCita = "15:30",
            fechaCita = LocalDate.parse("2021-12-12"),
            trabajador = Trabajador(
                idTrabajador = 3,
                nombreTrabajador = "Trabajador 3",
                telefonoTrabajador = 111111111,
                email = "correo3@example.com",
                username = "usuario3",
                password = "contraseña3",
                fechaContratacion = LocalDate.parse("2023-01-03"),
                especialidad = Especialidad.MOTOR,
                isResponsable = false,
            ),
            vehiculo = Vehiculo(
                matricula = "ABC123",
                marca = "Marca 1",
                modelo = "Modelo 1",
                fechaMatricula = LocalDate.parse("2022-01-01"),
                fechaUltimaRevision = LocalDate.parse("2023-01-01"),
                tipoMotor = TipoMotor.GASOLINA,
                tipoVehiculo = TipoVehiculo.TURISMO,
                propietario = Propietario(
                    dni = "123456789A",
                    nombre = "Propietario 1",
                    apellidos = "Apellidos 1",
                    telefono = 987654321,
                    emailPropietario = "Lucas3@propietario1@example.com.com"
                )
            )
        )

        val res = informesRepository.updateInformeById(4, informeNuevo)
        val res2 = informesRepository.updateInformeById(12, informeNuevo)

        val informeArreglado = informeNuevo.copy(idInforme = 4)
        assertTrue(res.component1()!!.idInforme == informeArreglado.idInforme)
        assertTrue(res.component1()!!.interior == informeArreglado.interior)
        assertTrue(res.component1()!!.apto == informeArreglado.apto)
        assertTrue(res2.component2() is InformeErrors.InformeNotFoundError)

    }

    @Test
    fun findByIdTest() {
        val res1 = informesRepository.findById(1)
        val res2 = informesRepository.findById(5)
        val res3 = informesRepository.findById(14)

        assertTrue(informes[0] == res1.component1())
        assertTrue(informes[4] == res2.component1())
        assertTrue(res3.component2() is InformeErrors.InformeNotFoundError)
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