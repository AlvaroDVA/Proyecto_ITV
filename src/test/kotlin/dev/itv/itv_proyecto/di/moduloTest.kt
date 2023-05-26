package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.repositories.InformeRepositoryImpl
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.database.DatabaseManager
import dev.itv.itv_proyecto.services.storages.JsonInformesStorage
import org.koin.dsl.module

val moduloTest = module {

    factory { AppConfig() }
    factory { DatabaseManager() }
    factory() { RoutesManager() }
    factory<TrabajadorRepositoryImpl>() { TrabajadorRepositoryImpl() }
    factory { JsonInformesStorage() }
    factory { InformeRepositoryImpl() }

}