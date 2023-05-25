package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import dev.itv.itv_proyecto.repositories.TrabajadorRepositoryImpl
import dev.itv.itv_proyecto.routes.RoutesManager
import dev.itv.itv_proyecto.services.database.DatabaseManager
import org.koin.core.scope.get
import org.koin.dsl.module

val Modulo = module {
    single { AppConfig() }
    single { DatabaseManager() }
    single { RoutesManager() }
    factory<TrabajadorRepositoryImpl> { TrabajadorRepositoryImpl()}
}