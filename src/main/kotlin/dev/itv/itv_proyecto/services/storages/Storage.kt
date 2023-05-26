package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import java.io.File
import java.sql.Connection

interface Storage <T> {

    fun saveFile(list: List<T>, url: String): Result<File, StorageErrors>

    fun loadFile (url : String, conexion : Connection) : Result<List<T>, StorageErrors>

}