create database IF NOT EXISTS bbITV ;
use bbITV;
#CREACION DE TABLAS
CREATE TABLE IF NOT EXISTS tEstacion (
    nID_Estacion int AUTO_INCREMENT PRIMARY KEY ,
    cNombre varchar (100)    NOT NULL ,
    cDireccion varchar (200) NOT NULL ,
    nTelefono int (9) NOT NULL ,
    cCorreo  varchar (100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tTrabajador(
    nId_Trabajador INT AUTO_INCREMENT PRIMARY KEY,
    cNombre VARCHAR(100) NOT NULL ,
    nTelefono VARCHAR(20) NOT NULL , # esto deberia ser un int de 9
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

#Tabla donde se almacenaran los datos cuando salte el disparador Actualizacion_tInforme
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
VALUES ('Trabajador 6', '611111111', 'email6@example.com', 'usuario6', 'contrasena6', '2000-05-01', 'INTERIOR', 1750, TRUE, 4);


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

INSERT INTO tVehiculo (cMatricula, cMarca, cModelo, dFecha_matriculacion, dFecha_ultima_revision, tipo_motor, tipo_vehiculo, cDNI_Propietario)
VALUES ('2323KLI', 'fiat', '500', '2023-01-01', '2022-12-01', 'DIESEL', 'TURISMO', '654321D');

#Insert para tabla tInforme
INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (TRUE, 7.5, 30.5, TRUE, TRUE, 1, '2020KKK', '09:30', '2022-01-01');

INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (FALSE, 5.2, 42.8, FALSE, FALSE, 2, '8956LLL', '10:00', '2022-02-02');

INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (TRUE, 4.7, 28.9, TRUE, TRUE, 3, '6666HKY', '11:00', '2022-03-03');

INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (FALSE, 6.8, 35.2, FALSE, TRUE, 4, '1234ABC', '11:30', '2022-04-04');

INSERT INTO tInforme (bFavorable, nFrenado, nContaminacion, bInterior, bLuces, nId_Trabajador, cMatricula, cHora, dFecha_Cita)
VALUES (null, null , null, null, null, 2, '2323KLI', '13:30', '2023-4-01');





delimiter $$
CREATE PROCEDURE trabajadoresPorEstacion (in Estacion INT)
    BEGIN

    DECLARE Nombre varchar (100);
    DECLARE Telefono int(9);
    DECLARE Email varchar (100);
    DECLARE Especialidad VARCHAR (20);
    DECLARE Salario INT ;
    DECLARE error BOOLEAN ;
    DECLARE cur1 CURSOR FOR
        SELECT cNombre, nTelefono, cEmail, cEspecialidad, nSalario
        FROM tTrabajador
        WHERE nID_Estacion=Estacion;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET error = TRUE;
    SET error = FALSE;

    OPEN cur1;
        loop1: LOOP
            FETCH cur1 INTO Nombre, Telefono, Email, Especialidad,Salario;
            Select Nombre,Telefono,Email,Especialidad,Salario;
            IF error THEN
                LEAVE loop1;
            END IF;
        end LOOP;
    CLOSE cur1;
    END $$
DELIMITER ;




#Trigger para qeu en cada actualizacion de tInforme se guarden los datos en tInforme_actualizaciones
DELIMITER $$
CREATE TRIGGER Actualizacion_tInforme
AFTER UPDATE ON tInforme
FOR EACH ROW
BEGIN
    INSERT INTO tInforme_actualizaciones (
        nId_informe, favorable_old, frenado_old, contaminacion_old,
        interior_old, luces_old, hora_old, fecha_cita_old,
        favorable_new, frenado_new, contaminacion_new,
        interior_new, luces_new, hora_new, fecha_cita_new
    )
    VALUES (
        OLD.nId_informe, OLD.bFavorable, OLD.nFrenado, OLD.nContaminacion,
        OLD.bInterior, OLD.bLuces, OLD.cHora, OLD.dFecha_Cita,
        NEW.bFavorable, NEW.nFrenado, NEW.nContaminacion,
        NEW.bInterior, NEW.bLuces, NEW.cHora, NEW.dFecha_Cita
    );
END $$
DELIMITER ;





# Procedimiento para borrar todas las citas con fecha de mas de dos meses con respecto a la fecha actual
DELIMITER $$
CREATE PROCEDURE BorrarCitasBimestrales()
BEGIN
    DELETE FROM tInforme
    WHERE dFecha_Cita <= CURDATE() - INTERVAL 2 MONTH;
END;




# Evento para que se ejecute un procedimiento cada dos meses
CREATE EVENT IF NOT EXISTS EventoBorrar
ON SCHEDULE
    EVERY 2 MONTH
    STARTS CURDATE()
DO
    CALL BorrarCitasBimestrales();



# Trigger para que actualice sueldo segun la especialidad del trabajador

DELIMITER $$

CREATE TRIGGER salario_trabajador
BEFORE INSERT ON tTrabajador
FOR EACH ROW
BEGIN
    CASE NEW.cEspecialidad
        WHEN 'ADMINISTRACION' THEN SET NEW.nSalario = 1650;
        WHEN 'ELECTRICIDAD' THEN SET NEW.nSalario = 1800;
        WHEN 'MOTOR' THEN SET NEW.nSalario = 1700;
        WHEN 'MECANICA' THEN SET NEW.nSalario = 1600;
        WHEN 'INTERIOR' THEN SET NEW.nSalario = 1750;
    END CASE;
END $$

DELIMITER ;



