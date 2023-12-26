module es.unex.cum.si.practica {
    requires javafx.controls;
    requires javafx.fxml;

    opens es.unex.cum.si.practica.controller to javafx.fxml;
    exports es.unex.cum.si.practica.controller;
    exports es.unex.cum.si.practica.model.genotype;
    exports es.unex.cum.si.practica.model.fenotype;
    exports es.unex.cum.si.practica.model.util;
}