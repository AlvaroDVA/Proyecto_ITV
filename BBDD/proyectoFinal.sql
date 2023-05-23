create database IF NOT EXISTS bbITV ;
use bbITV;
#CREACION DE TABLAS
CREATE TABLE IF NOT EXISTS tEstacion (
    nID_Estacion INT AUTO_INCREMENT PRIMARY KEY ,
    cNombre varchar (100)    NOT NULL ,
    cDireccion varchar (200) NOT NULL ,
    nTelefono int (9) NOT NULL ,
    cCorreo  varchar (100) NOT NULL
);

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

CREATE TABLE IF NOT EXISTS tPropietario (
    cDNI VARCHAR(20) PRIMARY KEY ,
    cNombre VARCHAR(100) NOT NULL ,
    cApellidos VARCHAR(100) NOT NULL ,
    nTelefono INT(20) NOT NULL ,
    cCorreo_electronico VARCHAR(100) UNIQUE NOT NULL
);


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


CREATE TABLE IF NOT EXISTS tInforme (   #esto deberia ser tabla citas
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



# Insert para tabla tEstacion
INSERT INTO tEstacion (cNombre, cDireccion, nTelefono, cCorreo)
VALUES ('Estación 1', 'c/leganes 21', 912345678, 'correo1@gmail.com');

INSERT INTO tEstacion (cNombre, cDireccion, nTelefono, cCorreo)
VALUES ('Estación 2', 'c/Paseo de la ermita 20', 987654321, 'correo2@gmail.com');

INSERT INTO tEstacion (cNombre, cDireccion, nTelefono, cCorreo)
VALUES ('Estación 3', 'Avd/Madagascar 33', 555555555, 'correo3@egmail.com');

INSERT INTO tEstacion (cNombre, cDireccion, nTelefono, cCorreo)
VALUES ('Estación 4', 'Avd/Fuenlabrada 4', 111111111, 'correo4@gmail.com');


# Inserta para tabla tTrabajador

INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion)
VALUES ('Trabajador 1', '667788991', 'email1@example.com', 'usuario1', 'contrasena1', '2022-01-01', 'ADMINISTRACION', 1650, FALSE, 1);

INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion)
VALUES ('Trabajador 2', '611111112', 'email2@example.com', 'usuario2', 'contrasena2', '2012-02-01', 'ELECTRICIDAD', 1800, FALSE, 2);

INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion)
VALUES ('Trabajador 3', '69988882', 'email3@example.com', 'usuario3', 'contrasena3', '2022-03-01', 'MOTOR', 1700, FALSE, 3);

INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion)
VALUES ('Trabajador 4', '68877998', 'email4@example.com', 'usuario4', 'contrasena4', '2022-04-01', 'MECANICA', 1600, FALSE, 4);

INSERT INTO tTrabajador (cNombre, nTelefono, cEmail, cUsuario, cContrasena, dFecha_contratacion, cEspecialidad, nSalario, nEs_jefe, nID_Estacion)
VALUES ('Trabajador 5', '68877998', 'email5@example.com', 'usuario5', 'contrasena5', '2010-05-01', 'INTERIOR', 17500, TRUE, 4);


# Insert para tabla tPropietario
INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico)
VALUES ('12345678A', 'Alejandro', 'Rodríguez', 123443889, 'Alejandro1@example.com');

INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico)
VALUES ('87654321B', 'Valentina', 'Fernández', 987684321, 'Valentina2@example.com');

INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico)
VALUES ('7654321C', 'Lucas', 'García', 551115555, 'Lucas3@example.com');

INSERT INTO tPropietario (cDNI, cNombre, cApellidos, nTelefono, cCorreo_electronico)
VALUES ('654321D', 'Sofía', ' Martínez', 111555111, 'Sofía4@example.com');


#Insert para tabla tVehiculo
INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario)
VALUES ('1234ABC', 'Opel', 'Astra', '2020-01-01', '2022-01-01', 'GASOLINA', 'TURISMO', '654321D');

INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario)
VALUES ('6666HKY', 'Toyota', 'Corolla', '2020-05-15', '2022-05-15', 'ELECTRICO', 'TURISMO', '7654321C');

INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario)
VALUES ('8956LLL', 'Ford', 'Focus', '2019-07-10', '2021-07-10', 'HIBRIDO', 'TURISMO', '87654321B');

INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario)
VALUES ('2020KKK', 'Volkswagen', 'Golf', '2015-01-20', '2023-01-20', 'DIESEL', 'TURISMO', '12345678A');


#Insert para tabla tInforme
INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (TRUE, 7.5, 30.5, TRUE, TRUE, 1, '2020KKK', '09:30', '2022-01-01');

INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (FALSE, 5.2, 42.8, FALSE, FALSE, 2, '8956LLL', '10:00', '2022-02-02');

INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (TRUE, 4.7, 28.9, TRUE, TRUE, 3, '6666HKY', '11:00', '2022-03-03');

INSERT INTO tInforme (bFavorable, cFrenado, cContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (FALSE, 6.8, 35.2, FALSE, TRUE, 4, '1234ABC', '11:30', '2022-04-04');