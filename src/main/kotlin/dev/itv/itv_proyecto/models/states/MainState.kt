package dev.itv.itv_proyecto.models.states

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate

data class MainState (
    val dniPropietario : SimpleStringProperty = SimpleStringProperty(),
    val nombrePropietario : SimpleStringProperty = SimpleStringProperty(),
    val apellidosPropietario : SimpleStringProperty = SimpleStringProperty(),
    val telefonoPropietario : SimpleStringProperty = SimpleStringProperty(),
    val emailPropietario : SimpleStringProperty = SimpleStringProperty(),

    val idTrabajador : SimpleStringProperty = SimpleStringProperty(),
    val nombreTrabajador : SimpleStringProperty = SimpleStringProperty(),
    val emailTrabajador : SimpleStringProperty = SimpleStringProperty(),

    val idInforme : SimpleStringProperty = SimpleStringProperty(),
    val frenadoInforme : SimpleStringProperty = SimpleStringProperty(),
    val contaminacionInforme : SimpleStringProperty = SimpleStringProperty(),
    val trabajadorInforme : SimpleStringProperty = SimpleStringProperty(),
    val matriculaInforme : SimpleStringProperty = SimpleStringProperty(),
    val dniInforme : SimpleStringProperty = SimpleStringProperty(),
    val horaCita : SimpleStringProperty = SimpleStringProperty(),
    val fechaCita : SimpleObjectProperty<LocalDate> = SimpleObjectProperty(),
    val interiorInforme : SimpleBooleanProperty = SimpleBooleanProperty(),
    val lucesInforme : SimpleBooleanProperty = SimpleBooleanProperty(),
    val apto : SimpleBooleanProperty = SimpleBooleanProperty(),

    val matriculaVehiculo : SimpleStringProperty = SimpleStringProperty(),
    val marcaVehiculo : SimpleStringProperty = SimpleStringProperty(),
    val modeloVehiculo : SimpleStringProperty = SimpleStringProperty(),
    val fechaMatriculacion : SimpleObjectProperty<LocalDate> = SimpleObjectProperty(),
    val ultimaRevision : SimpleObjectProperty<LocalDate> = SimpleObjectProperty(),
    val tipoMotor : SimpleStringProperty = SimpleStringProperty(),
    val tipoVehiculo : SimpleStringProperty = SimpleStringProperty(),

    val dniVehiculo : SimpleStringProperty = SimpleStringProperty()
) {

}