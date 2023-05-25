package dev.itv.itv_proyecto.utils

import java.sql.Connection

class UtilsForTest {
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
        inserts.add("INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 9.5, 40.0, 1, 1, 1, 'ABC123', '10:00', '2023-01-10');")
        inserts.add("INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (0, 5.0, 30.0, 0, 1, 2, 'XYZ789', '11:00', '2023-01-11');")
        inserts.add("INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 8.0, 25.0, 1, 0, 3, 'QWE456', '12:00', '2023-01-12');")
        inserts.add("INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (0, 6.5, 35.0, 0, 0, 4, 'ASD789', '13:00', '2023-01-13');")
        inserts.add("INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita) VALUES (1, 7.8, 42.0, 1, 1, 5, 'ZXC321', '14:00', '2023-01-14');")

        return inserts
    }

}