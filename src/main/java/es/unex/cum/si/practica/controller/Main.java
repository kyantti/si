package es.unex.cum.si.practica.controller;

import es.unex.cum.si.practica.model.fenotype.Driver;
import es.unex.cum.si.practica.model.genotype.Class;
import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Driver driver = new Driver();
        Schedule schedule = driver.run();
        for (Group group : Data.getInstance().getGroups().values()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/es/unex/cum/si/practica/view/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage groupStage = new Stage();
            groupStage.setTitle("Curso " + group.id());
            groupStage.setScene(scene);
            groupStage.show();

            Controller controller = fxmlLoader.getController();
            for (Class aClass : schedule.getClasses()) {
                if (aClass.groupId() == group.id()) {
                    controller.show(aClass);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
