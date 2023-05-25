package dev.itv.itv_proyecto.services.database

import dev.itv.itv_proyecto.config.AppConfig
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection
import java.sql.DriverManager

class DatabaseManager : KoinComponent{

    private val logger = KotlinLogging.logger {}

    val appConfig: AppConfig by inject()

    val bd: Connection by lazy {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection("jdbc:mariadb://${appConfig.bdPath}:3306/${appConfig.bdName}", "root", "")
        } catch (e: Exception) {
            logger.error("Error al establecer la conexión con la base de datos", e)
            throw e
        }
    }

    init {
        logger.debug { "Iniciando DataBaseManager()" }
        //Si quisiésemos hacer un drop a las tablas
        dropAllTables(false)
        createTables()
    }
    
    fun createTables() {
        logger.debug { " Creando Tablas " }
        crearTablaEstacion()
        crearTablaTrabajador()
        crearTablaPropietario()
        crearTablaVehiculo()
        crearTablaInformes()
    }

    private fun crearTablaPropietario() {
        logger.debug { "Creando tabla tPropietario" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tPropietario (
                cDNI VARCHAR(20) PRIMARY KEY ,
                cNombre VARCHAR(100) NOT NULL ,
                cApellidos VARCHAR(100) NOT NULL ,
                nTelefono INT(20) NOT NULL ,
                cCorreo_electronico VARCHAR(100) UNIQUE NOT NULL
            );
        """.trimIndent()

        this.bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaVehiculo() {
        logger.debug { "Creando tabla tVehiculos" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tVehiculo (
                cMatricula VARCHAR(10) PRIMARY KEY ,
                cMarca VARCHAR(50) NOT NULL ,
                cModelo VARCHAR(50) NOT NULL ,
                dFecha_matriculacion DATE NOT NULL ,
                dFecha_ultima_revision DATE NOT NULL ,
                tipo_motor ENUM('GASOLINA', 'DIESEL', 'ELECTRICO', 'HIBRIDO') NOT NULL ,
                tipo_vehiculo ENUM('TURISMO', 'FURGONETA', 'CAMION', 'MOTOCICLETA') NOT NULL,

                cDNI_Propietario VARCHAR(20) NOT NULL,
                FOREIGN KEY  (cDNI_Propietario) REFERENCES tPropietario (cDNI)
                                   ON UPDATE NO ACTION
                                   ON DELETE  NO ACTION

            );
        """.trimIndent()

        this.bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaInformes() {
        logger.debug { "Creando tabla tInformes" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tInforme (  
                nId_informe INT AUTO_INCREMENT PRIMARY KEY ,
                bFavorable BOOLEAN ,  # CAMPO CALCULADO SI CUMPLE TODOS LOS REQUISITOS
                cFrenado DECIMAL (4,2) CHECK( cFrenado >=1.0 AND cFrenado <=10.0),
                cContaminacion DECIMAL (4,2) CHECK( cContaminacion >=20.0 AND cContaminacion <=50.0),
                bInterior BOOLEAN,
                bLuces BOOLEAN,
                nId_Trabajador INT NOT NULL ,
                cMatricula VARCHAR(10) not null NOT NULL ,
                cHora VARCHAR (5),
                dFecha_Cita DATE,

                FOREIGN KEY (nId_Trabajador) REFERENCES tTrabajador(nId_Trabajador)
                    ON DELETE NO ACTION
                    ON UPDATE NO ACTION,
                    FOREIGN KEY (cMatricula) REFERENCES tVehiculo(cMatricula)
                    ON DELETE NO ACTION
                    ON UPDATE NO ACTION
            );
        """.trimIndent()

        this.bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaTrabajador() {
        logger.debug { "Creando Tabla tTrabajadores" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tTrabajador(
                nId_Trabajador INT AUTO_INCREMENT PRIMARY KEY,
                cNombre VARCHAR(100) NOT NULL ,
                nTelefono VARCHAR(20) NOT NULL ,
                cEmail VARCHAR(100) UNIQUE NOT NULL ,
                cUsuario VARCHAR(50) UNIQUE NOT NULL ,
                cContrasena VARCHAR(100) NOT NULL ,
                dFecha_contratacion DATE NOT NULL ,
                cEspecialidad ENUM ('ADMINISTRACION', 'ELECTRICIDAD', 'MOTOR', 'MECANICA', 'INTERIOR') NOT NULL ,
                nSalario INT ,  #CAMPO CALCULADO DEPENDIENDO DE LA ESPECIALIDAD
                nEs_jefe BOOLEAN  ,
                nID_Estacion INT NOT NULL ,
                FOREIGN KEY  (nID_Estacion) references tEstacion (nID_Estacion)
                    ON UPDATE NO ACTION
                    ON DELETE NO ACTION
            );
        """.trimIndent()

        this.bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaEstacion() {
        logger.debug { "Creando tabla tEstacion si existiese" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tEstacion (
                nID_Estacion INT AUTO_INCREMENT PRIMARY KEY ,
                cNombre varchar (100)    NOT NULL ,
                cDireccion varchar (200) NOT NULL ,
                nTelefono int (9) NOT NULL ,
                cCorreo  varchar (100) NOT NULL
            );
        """.trimIndent()

        this.bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    fun dropAllTables(confirmar: Boolean) {
        logger.debug { " DropAllTables ($confirmar) " }
        if (confirmar) {
            dropTablaInforme()
            dropTablaTrabajador()
            dropTablaVehiculo()
            dropTablePropietario()
            dropTablaEstacion()
        }
    }

    private fun dropTablaInforme() {
        logger.debug { " Eliminando datos tInforme " }
        val sql = "DROP TABLE IF EXISTS tInforme;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaTrabajador() {
        logger.debug { " Eliminando datos tTrabajador " }
        val sql = "DROP TABLE IF EXISTS tTrabajador;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaVehiculo() {
        logger.debug { " Eliminando datos tVehiculo " }
        val sql = "DROP TABLE IF EXISTS tVehiculo;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablePropietario() {
        logger.debug { " Eliminando datos tPropietario " }
        val sql = "DROP TABLE IF EXISTS tPropietario;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaEstacion() {
        logger.debug { " Eliminando datos tEstacion " }
        val sql = "DROP TABLE IF EXISTS tEstacion;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

}