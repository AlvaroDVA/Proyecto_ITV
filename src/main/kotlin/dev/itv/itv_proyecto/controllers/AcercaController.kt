package dev.itv.itv_proyecto.controllers

import com.vaadin.open.Open
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink

class AcercaController {

    @FXML
    private lateinit var  marioBueno : Hyperlink
    @FXML
    private lateinit var alvaroDelVal : Hyperlink
    @FXML
    private lateinit var mohamadElsayed : Hyperlink

    fun initialize() {
        marioBueno.setOnAction { onLinkMario() }
        alvaroDelVal.setOnAction { onLinkAlvaro() }
        mohamadElsayed.setOnAction {onLinkMohamad()}
    }

    private fun onLinkMario() {
        Open.open("https://github.com/Maarioo25")
    }

    private fun onLinkAlvaro() {
        Open.open("https://github.com/AlvaroDVA")
    }

    private fun onLinkMohamad() {
        Open.open("https://github.com/Nose01")
    }




}