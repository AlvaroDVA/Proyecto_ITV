# Taller ITV (DAMller) - README

<div>
    <img src="HTML/src/Imagenes/DAMller.png" align="right" width="200">
    <p align="left">Este proyecto es un taller sobre inspección técnica de vehículos (ITV) que abarca diferentes aspectos de la informática, como Bases de Datos, Lenguajes de Marcas, Programación y Entornos de Desarrollo.</p>
</div>

## Descripción del Proyecto

En este taller ITV, hemos desarrollado una aplicación que permite gestionar y llevar un registro de los vehículos que pasan por una estación de ITV. La aplicación proporciona funcionalidades relacionadas con la gestión de datos, edición e inserción de citas nuevas y generación de informes.

## Tecnologías Utilizadas

El proyecto se ha desarrollado utilizando las siguientes tecnologías:

- Bases de Datos: Se ha utilizado una base de datos relacional programada en SQL MariaDB para almacenar la información de los vehículos, sus inspecciones, citas, trabajadores, etc a través de eventos, procedimientos, triggers y tablas. Se ha utilizado un editor de texto como es DataGrip para el desarrollo de la base de datos.

- Lenguajes de Marcas: Se han utilizado lenguajes de marcas como HTML, CSS y JavaScript, junto con el IDE Visual Studio Code para la creación de la web, a través de la cual se envían los datos a nuestra base de datos gracias a nuestro formulario online.

- Programación: El backend de la aplicación se ha desarrollado utilizando el lenguaje de programación Kotlin, junto con el uso de otras dependencias y el uso de vistas creadas con Scene Builder. Todo el código está probado y testeado.

- Entornos de Desarrollo: Para el desarrollo del proyecto, se han utilizado entornos de desarrollo integrados (IDEs) como IntellIJ, Visual Studio Code y DataGrip, junto con herramientas de control de versiones como Git combinandolo con GitKraken (para la fácil visualización de las ramas) y la metodología de trabajo GitFlow.

## Estructura del Proyecto

El proyecto sigue una estructura de directorios y archivos de la siguiente manera:

- El directorio `Diagramas_Y_Design` contiene los archivos relacionados con la lógica de la aplicación como los modelos de la base de datos, programación y lenguaje de marcas. También incluye el diseño previo de la página web.

- El directorio `BBDD` contiene el archivo `db.sql`, que contiene el esquema y los datos de la base de datos utilizada en la aplicación.

- El directorio `HTML` contiene los archivos relacionados con la web, como hojas de estilo CSS, documentos HTML, documentos JavaScript e imágenes.

- El resto de directorios y archivos como `src`, `gradle`, `.gitignore`, `build.gradle`, etc están relacionados con la parte de programación, con sus dependecias y con sus configuraciones.

## Instrucciones de Instalación y Uso

1. Clona o descarga el repositorio.

2. Configura el entorno virtual.

3. Crea la base de datos utilizando el archivo `db.sql` en tu servidor de bases de datos (XAMPP preferiblemente).

4. Ejecuta la aplicación utilizando el comando `./gradlew run` o accede a nuestra web a través del plugin Five Server de Visual Studio Code.

## Estimación del Presupuesto

El costo estimado de desarrollo de este proyecto, considerando un equipo de tres programadores, puede variar dependiendo de diversos factores, como la experiencia de los programadores, la duración del proyecto y la paga por hora de cada trabajador.

Considerando un promedio de 40 horas por semana trabajadas por cada programador, y un costo/honorario por hora de 11 euros, la estimación del presupuesto sería la siguiente:

- Horas semanales totales: 40 horas/semana
- Duración estimada del proyecto: 3 semanas
- Costo/honorario por hora: 11 euros/hora
- Trabajadores: 3 programadores Junior

Presupuesto estimado = (Horas semanales totales * Duración estimada del proyecto * Número de programadores) * Costo/honorario por hora

Presupuesto estimado = (40 * 3 * 3) * 11

Presupuesto estimado = 4000€

Este presupuesto se ha calculado simplemente teniendo en cuenta los gastos de salarios de los trabajadores, sin tener en cuenta licencias y otros gastos añadidos del proyecto.

## Contacto

GitHubs:

- <a href="https://github.com/Maarioo25">Mario Bueno López</a>
- <a href="https://github.com/AlvaroDVA">Álvaro del Val Arce</a>
- <a href="https://github.com/Nose01">Mohamad El Sayed</a>

Correos:
- mariobueno060@gmail.com
- alvarodelvalarce@hotmail.com
- knd10_1@hotmail.com
