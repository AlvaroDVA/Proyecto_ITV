package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.models.Informe
import java.io.File

class HtmlInformesStorage : Storage<Informe> {
    /**
     * Función que guarda los Informes en un fichero Html
     *
     * @param list Lista de Informes que se guaradarán
     * @param url Path donde se guardará el fichero Html
     * @return Devolvera el archivo con los datos o los posibles errores con Result
     * @see generarContenidoHTML
     */
    override fun saveFile(item : Informe, url: String): Result<File, StorageErrors> {
        val contenidoHTML = generarContenidoHTML(item)
        val archivoHTML = File(url)
        archivoHTML.writeText(contenidoHTML)
        return Ok(archivoHTML)
    }

    /**
     * Función que transforma la lista de informes en contenido Html como un StringBuilder
     */
    private fun generarContenidoHTML(informe: Informe): String {
        return buildString {
            val contenido = StringBuilder()
            contenido.append("<!DOCTYPE html>\n")
            contenido.append("<html lang=\\\"es\\\">\n")
            contenido.append("<head>\n")
            contenido.append("<title>Informes</title>\n")
            contenido.append("<meta charset=\"utf-8\">\n")
            contenido.append("</head>\n")
            contenido.append("<body>\n")

            contenido.append("<h1>Informe</h1>")
            contenido.append("<h2>Id del Informe : ${informe.idInforme}</h2>\n")
            contenido.append("<h2>Estado inspección : ${informe.apto}</h2>\n")
            contenido.append("<h2>Frenado : ${informe.frenado}</h2>\n")
            contenido.append("<h2>Contaminación : ${informe.contaminacion}</h2>\n")
            contenido.append("<h2>Interior : ${informe.interior}</h2>\n")
            contenido.append("<h2>Luces : ${informe.luces}</h2>\n")
            contenido.append("<h2>Id del Trabajador : ${informe.trabajador.idTrabajador}</h2>\n")
            contenido.append("<h2>Nombre del Trabajador : ${informe.trabajador.nombreTrabajador}</h2>\n")
            contenido.append("<h2>Email del Trabajador : ${informe.trabajador.email}</h2>\n")
            contenido.append("<h2>Matrícula Vehículo : ${informe.vehiculo.matricula}</h2>\n")
            contenido.append("<h2>Marca Vehículo : ${informe.vehiculo.marca}</h2>\n")
            contenido.append("<h2>Modelo Vehículo : ${informe.vehiculo.modelo}</h2>\n")
            contenido.append("<h2>${informe.vehiculo.fechaMatricula}</h2>\n")
            contenido.append("<h2>${informe.vehiculo.fechaUltimaRevision}</h2>\n")
            contenido.append("<h2>${informe.vehiculo.tipoMotor}</h2>\n")
            contenido.append("<h2>${informe.vehiculo.tipoVehiculo}</h2>\n")
            contenido.append("<h2>${informe.propietario.dni}</h2>\n")
            contenido.append("<h2>${informe.propietario.nombre}</h2>\n")
            contenido.append("<h2>${informe.propietario.apellidos}</h2>\n")
            contenido.append("<h2>${informe.propietario.telefono}</h2>\n")
            contenido.append("<h2>${informe.propietario.emailPropietario}</h2>\n")
            contenido.append("<h2>${informe.horaCita}</h2>\n")
            contenido.append("<h2>${informe.fechaCita}</h2>\n")

            contenido.append("</body>\n")
            contenido.append("</html>\n")

            return contenido.toString()
        }
    }

}