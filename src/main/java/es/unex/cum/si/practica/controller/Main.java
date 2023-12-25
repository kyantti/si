package es.unex.cum.si.practica.controller;

import es.unex.cum.si.practica.model.fenotype.GeneticAlgorithm;
import es.unex.cum.si.practica.model.fenotype.Population;
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
        // Get a Schedule object with all the available information.
        Schedule schedule = new Schedule();

        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(50, 0.01, 0.8, 1, 5);

        // Initialize population
        Population population = ga.initPopulation(Data.getInstance());

        // Evaluate population
        ga.evalPopulation(population, schedule);

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (!ga.isTerminationConditionMet(generation, 500) && !ga.isTerminationConditionMet(population)) {
            // Print fitness
            System.out.println("Generation: " + generation + ", Best fitness: " + population.getFittest(0).getFitness());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, Data.getInstance());

            // Evaluate population
            ga.evalPopulation(population, schedule);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        schedule.parseChromosome(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());
        //System.out.println("Quality: " + schedule.calcQuality());

        // Print classes
        System.out.println();
        Class[] classes = schedule.getClasses();
        int classIndex = 1;
        for (Class bestClass : classes) {
            System.out.println("Class " + classIndex + ":");
            System.out.println("Module: " +
                    Data.getInstance().getSubject(bestClass.getSubjectId()).getDenomination());
            System.out.println("Group: " +
                    Data.getInstance().getGroup(bestClass.getGroupId()).getId());
            System.out.println("Room: " +
                    Data.getInstance().getRoomSlot(bestClass.getRoomId()).getDenomination());
            System.out.println("Time: " +
                    Data.getInstance().getTimeSlot(bestClass.getTimeId()).getDenomination());
            System.out.println("-----");
            classIndex++;
        }

        for (Group group : Data.getInstance().getGroups().values()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/es/unex/cum/si/practica/view/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage groupStage = new Stage();
            groupStage.setTitle("Curso " + group.getId());
            groupStage.setScene(scene);
            groupStage.show();

            Controller controller = fxmlLoader.getController();
            for (Class aClass : classes) {
                if (aClass.getGroupId() == group.getId()) {
                    controller.show(aClass);
                }
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
