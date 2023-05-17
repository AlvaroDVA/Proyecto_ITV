module dev.itv.itv_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens dev.itv.itv_proyecto to javafx.fxml;
    exports dev.itv.itv_proyecto;
}