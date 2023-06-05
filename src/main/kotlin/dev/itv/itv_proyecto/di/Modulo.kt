package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.PropietarioRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.repositories.VehiculoRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.services.storages.CsvTrabajadoresStorage
import dev.itv.itv_proyecto.services.storages.HtmlInformesStorage
import dev.itv.itv_proyecto.services.storages.JsonCitasStorage
import dev.itv.itv_proyecto.services.storages.JsonInformeStorage
import dev.itv.itv_proyecto.viewmodels.CitaViewModel
import dev.itv.itv_proyecto.viewmodels.MainViewModel
import dev.itv.itv_proyecto.viewmodels.InformeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val Modulo = module {
    single { AppConfig() }
    single { DatabaseManager() }
    single { RoutesManager }

    // Repositorios
    factory { InformeRepositoryImpl() }
    factory { PropietarioRepositoryImpl() }
    factory { TrabajadorRepositoryImpl() }
    factory { VehiculoRepositoryImpl() }

    // Storages
    factory { JsonInformeStorage() }
    factory { CsvTrabajadoresStorage() }
    factory { HtmlInformesStorage() }
    factory { JsonCitasStorage() }

    // ViewModel
    singleOf(::MainViewModel)
    factoryOf(::CitaViewModel)
    factoryOf(::InformeViewModel)

}

