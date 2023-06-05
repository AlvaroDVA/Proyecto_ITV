package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.services.database.DatabaseManager
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File


class JsonCitasStorage() : Storage<List<Informe>> , KoinComponent {

    val manager: DatabaseManager by inject()

    /**
     * Función que guarda los trabajadores en un fichero json
     *
     * @param list Lista de Informes que se guaradarán
     * @param url Path donde se guardará el fichero Json
     * @return Devolvera el archivo con los datos o los posibles errores con Result
     */
    override fun saveFile(lista: List<Informe>, url: String): Result<File, StorageErrors> {
        val logger = KotlinLogging.logger { }
        logger.warn { "StorageInformeJson ---- SaveFile()" }
        val listaMap = lista.map { Mappers().toCita(it) }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            logger.warn { " GsonBuilder Creado " }
            val json = gson.toJson(listaMap)
            logger.warn { " Lista pasada a String " }
            val file = File(url).apply {
                writeText(json)
            }
            logger.warn { " File guardado " }
            Ok(file)
        } catch (e: Exception) {
            Err(StorageErrors.JsonStorageError("No se ha podido guardar el fichero: ${e.message}"))
        }
    }

}