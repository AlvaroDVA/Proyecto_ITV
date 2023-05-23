package dev.itv.itv_proyecto.services.database

import dev.itv.itv_proyecto.routes.RoutesManager
import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {

    val db: Connection by lazy {
        DriverManager.getConnection("jdbc:sqlite:${RoutesManager.appConfig.bdPath}")
    }





}