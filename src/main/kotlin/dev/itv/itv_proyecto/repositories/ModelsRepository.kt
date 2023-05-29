package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.Result

interface ModelsRepository <T, ID, ERROR> {

    fun findById(id : ID) : Result<T, ERROR>

    fun loadAll() : Result<List<T>, ERROR>

}