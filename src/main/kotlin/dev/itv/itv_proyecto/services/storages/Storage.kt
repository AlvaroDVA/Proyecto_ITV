package dev.itv.itv_proyecto.services.storages

import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.StorageErrors
import java.io.File

interface Storage <T> {

    fun saveFile(list: List<T>, url: String): Result<File, StorageErrors>

    fun loadFile (url : String) : Result<List<T>, StorageErrors>

}