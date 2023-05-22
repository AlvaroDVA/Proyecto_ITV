package dev.itv.itv_proyecto.di

import dev.itv.itv_proyecto.config.AppConfig
import org.koin.dsl.module

val Modulo = module {
    single { AppConfig() }
}