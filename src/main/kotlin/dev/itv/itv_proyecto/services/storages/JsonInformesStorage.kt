package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.itv.itv_proyecto.errors.StorageErrors
import dev.itv.itv_proyecto.mappers.Mappers
import dev.itv.itv_proyecto.models.Informe
import dev.itv.itv_proyecto.models.dto.InformeDto
import dev.itv.itv_proyecto.services.database.DatabaseManager
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileReader
import java.sql.Connection


class JsonInformesStorage() : Storage<Informe> , KoinComponent {

    val manager : DatabaseManager by inject()

    /**
     * Función que guarda los trabajadores en un fichero json
     *
     * @param list Lista de Informes que se guaradarán
     * @param url Path donde se guardará el fichero Json
     * @return Devolvera el archivo con los datos o los posibles errores con Result
     */
    override fun saveFile(list: List<Informe>, url: String): Result<File, StorageErrors> {
        val logger = KotlinLogging.logger {  }
        logger.warn { "StorageInformeJson ---- SaveFile()" }
        val listaMap = list.map { Mappers().toDto(it) }
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
        } catch (e : Exception) {
            Err(StorageErrors.JsonStorageError("No se ha podido guardar el fichero: ${e.message}"))
        }
    }

    /**
     * Función que carga los Informes de un fichero Json
     *
     * @param url Path donde se cargara el fichero Json
     * @return Devolvera la lista de informes o los posibles errores con Result
     */
    fun loadFile(url: String, conexion : Connection?): Result<List<Informe>, StorageErrors> {
        val logger = KotlinLogging.logger {  }
        val database = conexion ?: manager.bd
        logger.warn { " StorageInformeJson ---- LoadFile() " }
        val file = File(url)
        logger.info { " Comprobando si el fichero existe " }
        if (!file.exists()) {
            // El archivo no existe
            return Err(StorageErrors.JsonStorageError("No existe un fichero en $url"))
        }
        return try {
            val mapper = Mappers()
            logger.warn { " Leyendo Fichero " }
            val gson = Gson()
            val reader = FileReader(file)


            val listType = object : TypeToken<List<InformeDto>>() {}.type
            val lista = gson.fromJson<List<InformeDto>>(reader, listType)
            Ok(lista.map { mapper.informeDtoToInforme(database ,it)?: return Err(StorageErrors.JsonStorageError("No se puede cargar los informes .")) })
        } catch (e : Exception) {
            // Error al leer el archivo
            Err(StorageErrors.JsonStorageError("No se puede leer los informes $e"))
        }
    }

}