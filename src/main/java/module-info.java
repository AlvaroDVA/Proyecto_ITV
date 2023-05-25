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

    // Para los test se necesita un módulo propio en la carpeta test

    opens dev.itv.itv_proyecto.controllers to javafx.fxml;
    exports dev.itv.itv_proyecto;

    opens dev.itv.itv_proyecto.repositories to javafx.fxml;
    exports dev.itv.itv_proyecto.repositories;


}