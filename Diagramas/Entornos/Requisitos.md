## Requisitos Funcionales ITV

1. Almacenar la información de los trabajadores de una ITV.
2. Gestionar Inspecciones.
3. Emitir informes sobre las citas.
4. Recoger datos sobre los vehículos.
5. Recoger datos sobre los propietarios.
6. Exportar información sobre trabajadores y citas.
7. Enviar información de un formulario web por Correo con la información de un propietario y su vehículo.

## Requisitos no funcionales

1. La información de los trabajadores a almacenar debe ser:
    - `idTrabajador`: UUID
    - `Nombre`: String
    - `Usuario`: String (Campo Único)
    - `Contraseña`: String (Cifrada)
    - `Teléfono`: Int
    - `Email`: String (Campo Único)
    - `Fecha de contratación`: LocalDate
    - `Especialidad`: Solo puede tener una de las siguientes opciones:
        - Administración
        - Electricidad
        - Motor
        - Mecánica
        - Interior
    - El sueldo se calculará en función de la especialidad y se sumará un plus de 100 cada 3 años trabajados. Los sueldos para cada especialidad son:
        - Administración: 1650.0
        - Electricidad: 1800.0
        - Motor: 1700.0
        - Mecánica: 1600.0
        - Interior: 1750.0
    - Además, habrá un campo para indicar si este trabajador es Responsable de la ITV (solo puede haber uno) y tiene un plus de 1000 Euros.

2. Las citas se deben gestionar en intervalos de 30 minutos por trabajador, siempre y cuando este esté libre. Cada trabajador no puede atender más de 4 citas por intervalo, y no puede haber más de 8 citas en el mismo intervalo.
    - Para atender las citas se necesitarán los datos del vehículo y del propietario.

3. La información a incluir en el informe será:
    - `Favorable`: Boolean
    - `Frenado` (Limitado entre 1 y 10 con dos decimales): Double
    - `Contaminación` (Limitado entre 20 y 50 con dos decimales): Double
    - `Interior`: Boolean
    - `Luces`: Boolean
    - Datos del trabajador (solo los que se quieren mostrar al propietario):
        - `idTrabajador`
        - `Nombre`
        - `Email`
    - Datos del vehículo
    - Datos del propietario

4. Los datos de los vehículos a almacenar deben ser:
    - `idVehiculo`: Long - Id proporcionada por la BD
    - `Marca`: String
    - `Modelo`: String
    - `Matricula`: String (Campo único)
    - `Fecha de matriculación`: LocalDate
    - `Fecha de última revisión`: LocalDateTime - Debe actualizarse al pasar la cita
    - `TipoMotor`: TipoMotor (El motor solo puede ser de uno de los siguientes tipos):
        - Gasolina
        - Diesel
        - Eléctrico
        - Híbrido
    - `TipoVehiculo`: TipoVehiculo (El tipo de vehículo solo puede ser uno de los siguientes tipos):
        - Turismo
        - Furgoneta
        - Camión
        - Motocicleta

5. Los datos de los propietarios a almacenar deben ser:
    - `DNI`: String (Campo Único PK)
    - `Nombre`: String
    - `Apellidos`: String
    - `Telefono`: Int
    - `CorreoElectronico`: String (Campo Único)

6. Se debe poder exportar la siguiente información:
    - Información de trabajadores: CSV
    - Citas: JSON
    - Informe: HTML (¿XML?) y JSON

7. La información del formulario debe enviarse con Mailto y validarse mediante JavaScript. Los campos a enviar deben ser:
    - `Nombre`
    - `Apellidos`
    - `CorreoElectronico`
    - `Telefono`
    - `DNI`
    - `Matricula del vehiculo`
    - `Marca`
    - `Modelo`
    - `Fecha Matriculación`
    - `Fecha ultima revisión`
    - `Tipo de Vehiculos`
    - `Tipo de Motor`
    - Botón para enviar datos

8. Toda la información debe guardarse en una BD de MariaDB con procedimientos, funciones, triggers y eventos para gestionarla.
    - Es necesario un procedimiento que se ocupe de listar los trabajadores que recibe como parámetro, de forma que se pueda leer de 1 en 1 con control de que no se ha llegado al final.
    - Debemos lanzar el procedimiento anterior para saber si el trabajador de la cita pertenece a esta inspección. Si es así, se cargarán los datos en la tabla "Informes".
    - Mediante un trigger se controlará si se actualiza alguna inspección, guardando la información previa y la información que se ha modificado.
    - Las citas deben borrarse de forma bimestral.

9. Se necesitará una aplicación de escritorio que permita gestionar las citas.
    - Se debe poder ver las citas existentes en una tabla y filtrar por matrícula y tipo de vehículo.
    - Se debe permitir dar de alta siempre y cuando se cumplan las restricciones, seleccionando en un combo al trabajador que pueda y añadiendo los datos del vehículo y del propietario.
    - Debe haber una ventana de "Acerca de" con información del programa.
    - Al seleccionar una cita, se podrán añadir los valores del informe e indicar si es apta o no.

## Requisitos de información

1. Los datos de los trabajadores son: idTrabajador, nombre, apellidos, username, contraseña, teléfono, email, especialidad y salario.
2. Los datos de la estación son: nombre, direccion, número de teléfono y correo electrónico.
3. Los datos del vehículo son: idVehiculo, marca, modelo, matricula, fecha de matriculación, fecha de última revisión, tipo de motor y tipo de vehículo.
4. Los datos del propietario son: Dni, nombre, apellidos, teléfono y correo electrónico.
5. Los tipos de motor son: Gasolina, diesel, electrico e hibrido.
6. Los tipos de vehículo son: Turismo, Furgoneta, Camión y Motocicleta.
7. Los datos del formulario son: Nombre, apellidos, correo electrónico, teléfono, matrícula del vehículo y Tipo de Vehículo.