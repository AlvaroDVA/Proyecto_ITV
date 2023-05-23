module dev.itv.itv_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires kotlin.logging.jvm;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.sql;


    opens dev.itv.itv_proyecto.controllers to javafx.fxml;
    exports dev.itv.itv_proyecto;
}