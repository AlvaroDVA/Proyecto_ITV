package dev.itv.itv_proyecto.repositories
import dev.itv.itv_proyecto.errors.VehiculosErrors
import dev.itv.itv_proyecto.models.Vehiculo

interface VehiculoRepository : CRUDRepository<Vehiculo, Long, VehiculosErrors> {
}