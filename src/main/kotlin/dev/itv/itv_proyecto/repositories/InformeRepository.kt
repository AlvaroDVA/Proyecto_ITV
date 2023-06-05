package dev.itv.itv_proyecto.repositories

import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.errors.InformeErrors
import dev.itv.itv_proyecto.models.Informe

interface InformeRepository : ModelsRepository<Informe, Long, InformeErrors> {

    fun saveInforme(informe: Informe) : Result<Informe, InformeErrors>

    fun deleteInformeById(id : Long) : Result<Informe,InformeErrors>

    fun updateInformeById(id : Long, informe: Informe) : Result<Informe,InformeErrors>

}