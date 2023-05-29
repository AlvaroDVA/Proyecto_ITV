# Taller ITV (DAMller) - README

<div>
    <img src="HTML/src/Imagenes/DAMller.png" align="right" width="200">
    <p align="left">Este proyecto es un taller sobre inspección técnica de vehículos (ITV) que abarca diferentes aspectos de la informática, como Bases de Datos, Lenguajes de Marcas, Programación y Entornos de Desarrollo.</p>
</div>

## Descripción del Proyecto

En este taller ITV, hemos desarrollado una aplicación que permite gestionar y llevar un registro de los vehículos que pasan por una estación de ITV. La aplicación proporciona funcionalidades relacionadas con la gestión de datos, edición e inserción de citas nuevas y generación de informes.

## Tecnologías Utilizadas

El proyecto se ha desarrollado utilizando las siguientes tecnologías:

- Bases de Datos: Se ha utilizado una base de datos relacional programada en SQL MariaDB para almacenar la información de los vehículos, sus inspecciones, citas, trabajadores, etc a través de eventos, procedimientos, triggers y tablas.

- Lenguajes de Marcas: Se han utilizado lenguajes de marcas como HTML, CSS y JavaScript para la creación de la web, a través de la cual se envían los datos a nuestra base de datos gracias a nuestro formulario online.

- Programación: El backend de la aplicación se ha desarrollado utilizando un lenguaje de programación Kotlin junto con el uso de otras dependencias y el uso de vistas creadas con Scene Builder.

- Entornos de Desarrollo: Para el desarrollo del proyecto, se han utilizado entornos de desarrollo integrados (IDEs) como IntellIJ y Visual Studio Code junto con herramientas de control de versiones como Git combinandolo con GitKraken (para la fácil visualización de las ramas) y la metodología de trabajo GitFlow.

## Estructura del Proyecto

El proyecto sigue una estructura de directorios y archivos de la siguiente manera:







- El directorio `app` contiene los archivos relacionados con la lógica de la aplicación, como los modelos de la base de datos y las vistas.

- El directorio `database` contiene el archivo `db.sql`, que contiene el esquema y los datos de la base de datos utilizada en la aplicación.

- El directorio `static` contiene los archivos estáticos de la aplicación, como hojas de estilo CSS e imágenes.

- El directorio `templates` contiene las plantillas HTML utilizadas para generar las páginas web de la aplicación.

## Instrucciones de Instalación y Uso

1. Clona o descarga el repositorio.

2. Configura el entorno virtual.

3. Crea la base de datos utilizando el archivo `db.sql` en tu servidor de bases de datos (XAMPP preferiblemente).

4. Ejecuta la aplicación utilizando el comando `./gradlew run` o accede a nuestra web a través del plugin Five Server de Visual Studio Code.

## Contacto

GitHubs:

- <a href="https://github.com/Maarioo25">Mario Bueno López</a>
- <a href="https://github.com/AlvaroDVA">Álvaro del Val Arce</a>
- <a href="https://github.com/Nose01">Mohamad El Sayed</a>

Correos:
- mariobueno060@gmail.com
- alvarodelvalarce@hotmail.com
- knd10_1@hotmail.com
