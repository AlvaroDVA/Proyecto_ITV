module dev.itv.itv_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires kotlin.logging.jvm;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.sql;
    requires koin.test.jvm;
    requires com.google.gson;
    requires kotlin.test;
    requires java.desktop;
    requires open;

    opens dev.itv.itv_proyecto.models.dto to com.google.gson, javafx.base;

    opens dev.itv.itv_proyecto.services.storages to javafx.fxml;
    exports dev.itv.itv_proyecto.services.storages;

    opens dev.itv.itv_proyecto.controllers to javafx.fxml;
    exports dev.itv.itv_proyecto;

    opens dev.itv.itv_proyecto.repositories to javafx.fxml;
    exports dev.itv.itv_proyecto.repositories;

    opens dev.itv.itv_proyecto.models to javafx.fxml;
    exports dev.itv.itv_proyecto.models;



}