package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.models.Trabajador
import dev.itv.itv_proyecto.utils.Utils
import java.io.File
import java.time.LocalDate

class CsvTrabajadoresStorage : Storage<Trabajador> {

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

    fun loadFile(url: String): Result<List<Trabajador>, StorageErrors> {
        val csvFile = File(url)
        val trabajadores = mutableListOf<Trabajador>()
        csvFile.bufferedReader().use { reader ->
            val csvLines = reader.readLines()
            val csvData = csvLines.drop(1)
            csvData.forEach { csvLine ->
                val values = csvLine.split(",")
                trabajadores.add(
                    Trabajador(
                        idTrabajador = values[0].toLong(),
                        nombreTrabajador = values[1],
                        telefonoTrabajador = values[2].toInt(),
                        email = values[3],
                        username = values[4],
                        password = values[5],
                        fechaContratacion = LocalDate.parse(values[6]),
                        especialidad = Utils.sacarEspecialidad(values[7]),
                        isResponsable = values[8].toBooleanStrictOrNull()?: return Err(StorageErrors.CsvStorageError("IsResponsable mal leido, no es false o true"))
                    )
                )
            }
        }
        if(trabajadores.isEmpty()) return (Err(StorageErrors.CsvStorageError("La lista leida del CSV esta vacia")))
        return Ok(trabajadores)
    }
}