package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import java.io.File

class HtmlInformesStorage : Storage<Informe> {
    override fun saveFile(list: List<Informe>, url: String): Result<File, StorageErrors> {
        if (list.isEmpty()) return Err(StorageErrors.HtmlStorageError("La lista de informes esta vacia"))
        val contenidoHTML = generarContenidoHTML(list)
        val archivoHTML = File(url)
        archivoHTML.writeText(contenidoHTML)
        return Ok(archivoHTML)
    }

    private fun generarContenidoHTML(informes: List<Informe>): String {
        val informesMap = informes.map { Mappers().toDto(it) }
        return buildString {
            val contenido = StringBuilder()
            contenido.append("<!DOCTYPE html>\n")
            contenido.append("<html lang=\\\"es\\\">\n")
            contenido.append("<head>\n")
            contenido.append("<title>Informes</title>\n")
            contenido.append("<meta charset=\"utf-8\">\n")
            contenido.append("</head>\n")
            contenido.append("<body>\n")
            contenido.append("<h1>Todos los Informes</h1>\n")
            contenido.append("<table> \n")
            contenido.append("<tr> \n")
            contenido.append("<th>idInforme</th>\n")
            contenido.append("<th>Apto</th>\n")
            contenido.append("<th>Frenado</th>\n")
            contenido.append("<th>Contaminación</th>\n")
            contenido.append("<th>Interior</th>\n")
            contenido.append("<th>Luces</th>\n")
            contenido.append("<th>IdTrabajador</th>\n")
            contenido.append("<th>NombreTrabajador</th>\n")
            contenido.append("<th>Email</th>\n")
            contenido.append("<th>Matricula</th>\n")
            contenido.append("<th>Marca</th>\n")
            contenido.append("<th>Modelo</th>\n")
            contenido.append("<th>FechaMatricula</th>\n")
            contenido.append("<th>FechaUltimaRevision</th>\n")
            contenido.append("<th>DNI</th>\n")
            contenido.append("<th>NombrePropietario</th>\n")
            contenido.append("<th>ApellidoPropietario</th>\n")
            contenido.append("<th>Telefono</th>\n")
            contenido.append("<th>EmailPropietario</th>\n")
            contenido.append("<th>HoraCita</th>\n")
            contenido.append("<th>FechaCita</th>\n")

            contenido.append("</tr>\n")

            for (informe in informesMap) {
                contenido.append("<tr>\n")
                contenido.append("<td>${informe.idInforme}</td>\n")
                contenido.append("<td>${informe.apto}</td>\n")
                contenido.append("<td>${informe.frenado}</td>\n")
                contenido.append("<td>${informe.contaminacion}</td>\n")
                contenido.append("<td>${informe.interior}</td>\n")
                contenido.append("<td>${informe.luces}</td>\n")
                contenido.append("<td>${informe.idTrabajador}</td>\n")
                contenido.append("<td>${informe.nombreTrabajador}</td>\n")
                contenido.append("<td>${informe.email}</td>\n")
                contenido.append("<td>${informe.matricula}</td>\n")
                contenido.append("<td>${informe.marca}</td>\n")
                contenido.append("<td>${informe.modelo}</td>\n")
                contenido.append("<td>${informe.fechaMatricula}</td>\n")
                contenido.append("<td>${informe.fechaUltimaRevision}</td>\n")
                contenido.append("<td>${informe.tipoMotor}</td>\n")
                contenido.append("<td>${informe.tipoVehiculo}</td>\n")
                contenido.append("<td>${informe.dni}</td>\n")
                contenido.append("<td>${informe.nombre}</td>\n")
                contenido.append("<td>${informe.apellidos}</td>\n")
                contenido.append("<td>${informe.telefono}</td>\n")
                contenido.append("<td>${informe.emailPropietario}</td>\n")
                contenido.append("<td>${informe.horaCita}</td>\n")
                contenido.append("<td>${informe.fechaCita}</td>\n")
                contenido.append("</tr>\n")
            }

            contenido.append("</table>\n")
            contenido.append("</body>\n")
            contenido.append("</html>\n")

            return contenido.toString()
        }
    }

}