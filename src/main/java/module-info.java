module es.unex.cum.si.practica {
    requires javafx.controls;
    requires javafx.fxml;

    exports es.unex.cum.si.practica.controller;
    opens es.unex.cum.si.practica.controller to javafx.fxml;
    exports es.unex.cum.si.practica.model.genotype;
}