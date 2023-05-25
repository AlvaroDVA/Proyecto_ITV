package dev.itv.itv_proyecto.repositories
import com.github.michaelbull.result.Result
import dev.itv.itv_proyecto.enums.TipoVehiculo
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Vehiculo

interface VehiculoRepository : CRUDRepository<Vehiculo, String, VehiculosErrors> {
    fun filterByMatricula (matricula : String) : Result<List<Vehiculo>, VehiculosErrors>
    fun filterByTipo(tipo : TipoVehiculo) : Result<List<Vehiculo>, VehiculosErrors>
}