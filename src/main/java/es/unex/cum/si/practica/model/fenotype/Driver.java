package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.io.*;
import java.util.Properties;
import java.util.Random;
import com.opencsv.CSVWriter;

public class Driver {
    private static final int ELITISM_K;
    private static final int POP_SIZE;
    private static final int MAX_ITER;
    private static final double MUTATION_RATE;
    private static final double CROSSOVER_RATE;
    private static final int REPLACEMENT_STRATEGY;
    private static final int SELECTION_ALGORITHM;
    private static final int TOURNAMENT_SIZE;
    private static final int TRUNCATION_SIZE;
    private static final int CROSSOVER_ALGORITHM;
    private static final int CROSSOVER_POINTS;
    private static final int MUTATION_ALGORITHM;

    static {
        // Load configuration from .config file
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/es/unex/cum/si/practica/config/config.properties"))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ELITISM_K = Integer.parseInt(properties.getProperty("ELITISM_K"));
        POP_SIZE = Integer.parseInt(properties.getProperty("POP_SIZE"));
        MAX_ITER = Integer.parseInt(properties.getProperty("MAX_ITER"));
        MUTATION_RATE = Double.parseDouble(properties.getProperty("MUTATION_RATE"));
        CROSSOVER_RATE = Double.parseDouble(properties.getProperty("CROSSOVER_RATE"));
        REPLACEMENT_STRATEGY = Integer.parseInt(properties.getProperty("REPLACEMENT_STRATEGY"));
        SELECTION_ALGORITHM = Integer.parseInt(properties.getProperty("SELECTION_ALGORITHM"));
        TOURNAMENT_SIZE = Integer.parseInt(properties.getProperty("TOURNAMENT_SIZE"));
        TRUNCATION_SIZE = Integer.parseInt(properties.getProperty("TRUNCATION_SIZE"));
        CROSSOVER_ALGORITHM = Integer.parseInt(properties.getProperty("CROSSOVER_ALGORITHM"));
        CROSSOVER_POINTS = Integer.parseInt(properties.getProperty("CROSSOVER_POINTS"));
        MUTATION_ALGORITHM = Integer.parseInt(properties.getProperty("MUTATION_ALGORITHM"));
    }



    /**
     * Selects a parent individual based on the specified selection algorithm.
     *
     * @param pop The population from which to select a parent.
     * @return The selected parent individual.
     */
    private static Individual selectParent(Population pop) {
        if (SELECTION_ALGORITHM == 0) {
            return pop.selectParentByRouletteWheel();
        }
        else if (SELECTION_ALGORITHM == 1) {
            return pop.rankSelection();
        }
        else if (SELECTION_ALGORITHM == 2) {
            return pop.selectParentByTournament(TOURNAMENT_SIZE);
        }
        else if (SELECTION_ALGORITHM == 3){
            return pop.selectParentByTruncation(TRUNCATION_SIZE);
        }
        else {
            return null;
        }
    }

    /**
     * Performs crossover between two parent individuals based on the specified
     * crossover algorithm and points.
     *
     * @param population The population to which parents belong.
     * @param parentA    The first parent individual.
     * @param parentB    The second parent individual.
     * @return An array of offspring individuals resulting from the crossover.
     */
    private static Individual[] crossover(Population population, Individual parentA, Individual parentB) {
        if (CROSSOVER_ALGORITHM == 0) {
            return population.onePointCrossover(parentA, parentB);
        }
        else if (CROSSOVER_ALGORITHM == 1 && CROSSOVER_POINTS > 1) {
            return population.nPointCrossover(parentA, parentB, CROSSOVER_POINTS);
        }
        else if (CROSSOVER_ALGORITHM == 2){
            return population.uniformCrossover(parentA, parentB);
        }
        else {
            return new Individual[0];
        }
    }

    /**
     * Applies mutation to the given individual based on the specified mutation algorithm.
     *
     * @param individual The individual to which mutation is applied.
     */
    private static void mutation(Individual individual) {
        if (MUTATION_ALGORITHM == 0) {
            individual.mutate(MUTATION_RATE);
        }
        else if (MUTATION_ALGORITHM == 1) {
            individual.mutate2();
        }
    }

    /**
     * Runs the evolutionary algorithm to generate a schedule.
     *
     * @return The generated schedule for teachers.
     * @throws IOException If an I/O error occurs during the process.
     */
    public Schedule run() throws IOException {

        Schedule schedule = new Schedule(Data.getInstance().getNumOfClasses());
        Population pop = new Population(POP_SIZE, Data.getInstance());
        Population newPopulation;
        Individual[] indiv = new Individual[2];
        Individual[] newIndiv;
        int i = 0;
        File outDir = new File("out");
        if (!outDir.exists()) {
            if (outDir.mkdirs()) {
                System.out.println("Directorio 'out' creado con éxito.");
            } else {
                System.err.println("Error al crear el directorio 'out'.");
            }
        }
        // Especifica el nombre del archivo CSV en el directorio out
        FileWriter outputfile = new FileWriter("out/resultados.csv");

        // Crea un CSVWriter con delimitador ',' (puedes cambiarlo según tus necesidades)
        CSVWriter csvWriter = new CSVWriter(outputfile);

        // Escribe la primera línea con los encabezados
        String[] headers = {"Generation", "Best Fitness", "Total Fitness"};
        csvWriter.writeNext(headers);
        String[] data = new String[3];

        pop.evalPopulation(schedule);

        while (i < MAX_ITER && pop.getFittest(0).getFitness() != 1) {

            System.out.println("Generation: " + i + ", Best fitness: " + pop.getFittest(0).getFitness() + ", Total fitness: " + pop.getFitness());
            data[0] = Integer.toString(i);
            data[1] = Double.toString(pop.getFittest(0).getFitness());
            data[2] = Double.toString(pop.getFitness());
            csvWriter.writeNext(data);

            newPopulation = new Population(pop.size());

            int j = 0;

            for (j = 0; j < ELITISM_K; j++) {
                newPopulation.setIndividual(j, pop.getFittest(j));
            }

            while (j < POP_SIZE) {

                indiv[0] = selectParent(pop);
                indiv[1] = selectParent(pop);

                if (REPLACEMENT_STRATEGY == 1) {
                    if (Math.random() < CROSSOVER_RATE) {

                        indiv = crossover(pop, indiv[0], indiv[1]);

                        if (Math.random() < MUTATION_RATE) {
                            mutation(indiv[0]);
                        }
                        if (Math.random() < MUTATION_RATE) {
                            mutation(indiv[1]);
                        }
                    }

                    newPopulation.setIndividual(j, indiv[0]);
                    j++;
                    newPopulation.setIndividual(j, indiv[1]);
                    j++;
                }
                else if (REPLACEMENT_STRATEGY == 0) {

                    newIndiv = crossover(pop, indiv[0], indiv[1]);

                    if (Math.random() < MUTATION_RATE) {
                        mutation(newIndiv[0]);
                    }
                    if (Math.random() < MUTATION_RATE) {
                        mutation(newIndiv[1]);
                    }

                    Random rdm = new Random();
                    newPopulation.setIndividual(j, indiv[rdm.nextInt(2)]);
                    j++;
                    newPopulation.setIndividual(j, newIndiv[rdm.nextInt(2)]);
                    j++;
                }

            }
            pop = newPopulation;
            pop.evalPopulation(schedule);
            i++;
        }

        schedule.parseChromosome(pop.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + i + " generations");
        System.out.println("Final solution fitness: " + pop.getFittest(0).getFitness() + ", Total fitness: " + pop.getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());
        //schedule.calcQuality();
        System.out.println("Dos: " + schedule);
        data[0] = Integer.toString(i);
        data[1] = Double.toString(pop.getFittest(0).getFitness());
        data[2] = Double.toString(pop.getFitness());
        csvWriter.writeNext(data);

        csvWriter.close();

        return schedule;
    }
}
