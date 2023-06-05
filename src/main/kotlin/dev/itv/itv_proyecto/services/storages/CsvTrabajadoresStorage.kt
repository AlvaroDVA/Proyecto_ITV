package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.models.Trabajador
import java.io.File

class CsvTrabajadoresStorage : Storage<List<Trabajador>> {

    /**
     * Función que guarda los trabajadores en un fichero csv
     *
     * @param list Lista de trabajadores que se guaradarán
     * @param url Path donde se guardará el fichero CSV
     * @return Devolvera el archivo con los datos o los posibles errores con Result
     */
    override fun saveFile(list: List<Trabajador>, url: String): Result<File, StorageErrors> {
        val csvFile = File(url)
        if (list.isEmpty()) return Err(StorageErrors.CsvStorageError("La lista ha guardar esta vacia"))
        csvFile.bufferedWriter().use { writer ->
            val csvHeader = listOf("IdTrabajador","Nombre", "Telefono", "Email" , "Usuario", "Contraseña", "Fecha_Contratación", "Especialidad", "IsResponsable")
            writer.write(csvHeader.joinToString(","))
            writer.newLine()
            list.forEach { trabajador ->
                val csvLine = listOf(
                    trabajador.idTrabajador.toString(),
                    trabajador.nombreTrabajador,
                    trabajador.telefonoTrabajador.toString(),
                    trabajador.email,
                    trabajador.username,
                    trabajador.password,
                    trabajador.fechaContratacion.toString(),
                    trabajador.especialidad.toString(),
                    trabajador.isResponsable.toString()
                )
                writer.write(csvLine.joinToString(","))
                writer.newLine()
            }
        }
        return Ok(csvFile)
    }


}