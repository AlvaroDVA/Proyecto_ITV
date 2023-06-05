package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.repositories.*
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.services.storages.CsvTrabajadoresStorage
import dev.itv.itv_proyecto.services.storages.HtmlInformesStorage
import dev.itv.itv_proyecto.services.storages.JsonInformeStorage
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val moduloTest = module {

    factory { AppConfig() }
    factory { DatabaseManager() }
    factory() { RoutesManager }
    factory<TrabajadorRepositoryImpl>() { TrabajadorRepositoryImpl() }
    factory { JsonInformeStorage() }
    factory { InformeRepositoryImpl() }
    factory { HtmlInformesStorage() }
    factory { TrabajadorRepositoryImpl() }
    factory { VehiculoRepositoryImpl() }
    factory { PropietarioRepositoryImpl() }
    factory { CsvTrabajadoresStorage() }

}