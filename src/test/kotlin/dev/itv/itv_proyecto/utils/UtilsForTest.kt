package dev.itv.itv_proyecto.utils

import mu.KotlinLogging
import java.sql.Connection

class UtilsForTest {
    private val logger = KotlinLogging.logger {  }

    fun initValoresBd(database: Connection) {
        val inserts = generateInserts()
        saveInserts(database,inserts)
    }

    private fun saveInserts(database: Connection, inserts: List<String>) {
        database.createStatement().use { statement ->
            for (insert in inserts) {
                statement.executeUpdate(insert)
            }
            statement.close()
        }
    }

    private fun generateInserts(): List<String> {
        val inserts = mutableListOf<String>()

        // INSERT para la tabla tEstacion
        inserts.add("INSERT INTO tEstacion (cNombre, cDireccion, nTelefono, cCorreo) VALUES ('Estacion 1', 'Dirección 1', 123456789, 'correo1@example.com');")

        // INSERTs para la tabla tTrabajador
        inserts.add("INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion) VALUES ('Trabajador 1', '123456789', 'correo1@example.com', 'usuario1', 'contraseña1', '2023-01-01', 'ADMINISTRACION', NULL, 1, 1);")
        inserts.add("INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion) VALUES ('Trabajador 2', '987654321', 'correo2@example.com', 'usuario2', 'contraseña2', '2023-01-02', 'ELECTRICIDAD', NULL, 0, 1);")
        inserts.add("INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion) VALUES ('Trabajador 3', '111111111', 'correo3@example.com', 'usuario3', 'contraseña3', '2023-01-03', 'MOTOR', NULL, 0, 1);")
        inserts.add("INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion) VALUES ('Trabajador 4', '222222222', 'correo4@example.com', 'usuario4', 'contraseña4', '2023-01-04', 'MECANICA', NULL, 0, 1);")
        inserts.add("INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion) VALUES ('Trabajador 5', '333333333', 'correo5@example.com', 'usuario5', 'contraseña5', '2023-01-05', 'INTERIOR', NULL, 0, 1);")

        // INSERTs para la tabla tPropietario
        inserts.add("INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico) VALUES ('123456789A', 'Propietario 1', 'Apellidos 1', 987654321, 'propietario1@example.com');")
        inserts.add("INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico) VALUES ('987654321B', 'Propietario 2', 'Apellidos 2', 123456789, 'propietario2@example.com');")
        inserts.add("INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico) VALUES ('555555555C', 'Propietario 3', 'Apellidos 3', 555555555, 'propietario3@example.com');")
        inserts.add("INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico) VALUES ('999999999D', 'Propietario 4', 'Apellidos 4', 999999999, 'propietario4@example.com');")
        inserts.add("INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico) VALUES ('777777777E', 'Propietario 5', 'Apellidos 5', 777777777, 'propietario5@example.com');")

        // INSERTs para la tabla tVehiculo
        inserts.add("INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario) VALUES ('ABC123', 'Marca 1', 'Modelo 1', '2023-01-01', '2022-01-01','GASOLINA', 'TURISMO', '123456789A');")
        inserts.add("INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario) VALUES ('XYZ789', 'Marca 2', 'Modelo 2', '2023-02-02', '2022-02-02','DIESEL', 'FURGONETA', '987654321B');")
        inserts.add("INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario) VALUES ('QWE456', 'Marca 3', 'Modelo 3', '2023-03-03', '2022-03-03','ELECTRICO', 'CAMION', '555555555C');")
        inserts.add("INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario) VALUES ('ASD789', 'Marca 4', 'Modelo 4', '2023-04-04', '2022-04-04', 'HIBRIDO', 'MOTOCICLETA', '999999999D');")
        inserts.add("INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario) VALUES ('ZXC321', 'Marca 5', 'Modelo 5', '2023-05-05', '2022-05-05', 'GASOLINA', 'TURISMO', '777777777E');")

        // INSERTs para la tabla tInforme
        inserts.add("INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 9.5, 40.0, 1, 1, 1, 'ABC123', '10:00', '2023-01-10');")
        inserts.add("INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (0, 5.0, 30.0, 0, 1, 2, 'XYZ789', '11:00', '2023-01-11');")
        inserts.add("INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 8.0, 25.0, 1, 0, 3, 'QWE456', '12:00', '2023-01-12');")
        inserts.add("INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (0, 6.5, 35.0, 0, 0, 4, 'ASD789', '13:00', '2023-01-13');")
        inserts.add("INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 7.8, 42.0, 1, 1, 5, 'ZXC321', '14:00', '2023-01-14');")

        return inserts
    }

    fun createTables(bd : Connection) {
        logger.debug { " Creando Tablas " }
        crearTablaEstacion(bd)
        crearTablaTrabajador(bd)
        crearTablaPropietario(bd)
        crearTablaVehiculo(bd)
        crearTablaInformes(bd)
        crearTablaInformesActualizaciones(bd)
    }

    private fun crearTablaPropietario(bd : Connection) {
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

        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaVehiculo(bd : Connection) {
        logger.debug { "Creando tabla tVehiculos" }

        val sql = """
           CREATE TABLE IF NOT EXISTS tVehiculo (
            cMatricula VARCHAR(10) PRIMARY KEY,
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

        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaInformes(bd : Connection) {
        logger.debug { "Creando tabla tInformes" }

        val sql = """
            CREATE TABLE IF NOT EXISTS tInforme(
                nId_informe INT AUTO_INCREMENT PRIMARY KEY ,
                bFavorable BOOLEAN ,
                nFrenado DECIMAL (4,2) CHECK( nFrenado >=1.0 AND nFrenado <=10.0),
                nContaminacion DECIMAL (4,2) CHECK( nContaminacion >=20.0 AND nContaminacion <=50.0),
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

        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaTrabajador(bd : Connection) {
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

        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaEstacion(bd : Connection) {
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

        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    fun dropAllTables(bd : Connection,confirmar: Boolean) {
        logger.debug { " DropAllTables ($confirmar) " }
        if (confirmar) {
            dropTablaInforme(bd)
            dropTablaTrabajador(bd)
            dropTablaVehiculo(bd)
            dropTablePropietario(bd)
            dropTablaEstacion(bd)
        }
    }

    private fun dropTablaInforme(bd : Connection) {
        logger.debug { " Eliminando datos tInforme " }
        val sql = "DROP TABLE IF EXISTS tInforme;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaTrabajador(bd : Connection) {
        logger.debug { " Eliminando datos tTrabajador " }
        val sql = "DROP TABLE IF EXISTS tTrabajador;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaVehiculo(bd : Connection) {
        logger.debug { " Eliminando datos tVehiculo " }
        val sql = "DROP TABLE IF EXISTS tVehiculo;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablePropietario(bd : Connection) {
        logger.debug { " Eliminando datos tPropietario " }
        val sql = "DROP TABLE IF EXISTS tPropietario;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun dropTablaEstacion(bd : Connection) {
        logger.debug { " Eliminando datos tEstacion " }
        val sql = "DROP TABLE IF EXISTS tEstacion;"
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

    private fun crearTablaInformesActualizaciones(bd : Connection) {
        logger.debug { "Creando tabla tInforme_actualizaciones" }
        val sql = """
            CREATE TABLE IF NOT EXISTS tInforme_actualizaciones (
                nId_informe INT,
                favorable_old BOOLEAN,
                frenado_old DECIMAL (4,2),
                contaminacion_old DECIMAL (4,2),
                interior_old BOOLEAN,
                luces_old BOOLEAN,
                hora_old VARCHAR (5),
                fecha_cita_old DATE,
                favorable_new BOOLEAN,
                frenado_new DECIMAL (4,2),
                contaminacion_new DECIMAL (4,2),
                interior_new BOOLEAN,
                luces_new BOOLEAN,
                hora_new VARCHAR (5),
                fecha_cita_new DATE
              );
        """.trimIndent()
        bd.createStatement().use {
            it.executeUpdate(sql)
        }
    }

}