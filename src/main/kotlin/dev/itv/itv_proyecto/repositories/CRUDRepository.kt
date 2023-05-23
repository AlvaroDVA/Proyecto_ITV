package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.Result

interface CRUDRepository <T, ID, ERROR> {

    fun save(item : T) : Result<T,ERROR>

    fun deleteById(id : ID) : Result<T, ERROR>

    fun findById(id : ID) : Result<T, ERROR>

    fun updateById(id : ID) : Result<T, ERROR>

}