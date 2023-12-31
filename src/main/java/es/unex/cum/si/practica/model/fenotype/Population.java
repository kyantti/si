package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Arrays;
import java.util.Random;

/**
 * The Population class stores an array of individuals and their total fitness. It also provides
 * methods to access and update the individuals.
 */
public class Population {
    private final Individual[] individuals;
    private double fitness = -1;

    /**
     * Constructs an empty population with the given size.
     *
     * @param populationSize The size of the population.
     */
    public Population(int populationSize) {
        this.individuals = new Individual[populationSize];
    }

    /**
     * Constructs a population with the given size and initializes individuals based on the provided data.
     *
     * @param populationSize The size of the population.
     * @param data           The data containing information about groups, timeslots, and rooms.
     */
    public Population(int populationSize, Data data) {
        // Initial individuals
        this.individuals = new Individual[populationSize];

        // Loop over individuals size
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create individual
            Individual individual = new Individual(data);
            // Add individual to individuals
            this.individuals[individualCount] = individual;
        }
    }

    /**
     * Gets the fittest individual at the specified offset in the population.
     *
     * @param offset The offset indicating the position of the fittest individual.
     * @return The fittest individual.
     */
    public Individual getFittest(int offset) {
        // Order individuals by fitness
        Arrays.sort(individuals, (o1, o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return 1;
            }
            return 0;
        });
        // Return the fittest individual
        return this.individuals[offset];
    }

    /**
     * The probability that an individual is selected is
     * proportional to their relative fitness, that is, to their fitness divided by the sum
     * of the fitness of all individuals in the population. If an individual's fitness is
     * double that of another, so will the probability of being selected. This
     * method presents problems if the fitness of a few individuals is much higher
     * (several orders of magnitude) to the rest, since these will be selected in a
     * repeated and almost all the individuals of the next generation will be “children” of the
     * same “parents” (little variation).
     *
     * @return The selected individual.
     */
    public Individual selectParentByRouletteWheel() {
        // Spin roulette wheel
        double rouletteWheelPosition = Math.random() * fitness;
        // Find parent
        double spinWheel = 0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return individuals[individuals.length - 1];
    }

    /**
     * The probability of selection of an individual is inversely
     * proportional to the position it occupies after ordering all the individuals from greatest to
     * lower fitness. This method is less aggressive than the roulette method when the
     * difference between the greatest fitness is several orders of magnitude higher than the rest.
     *
     * @return The selected individual.
     */
    public Individual rankSelection() {
        //sumatorio de los numeros consecutivos del ranking
        int totalranking = 0;
        for (int i = 1; i <= individuals.length; i++) {
            totalranking += i;
        }

        Individual[] copy = Arrays.copyOfRange(individuals, 0, individuals.length);
        double[] ranks = new double[individuals.length];

        Arrays.sort(copy, (o1, o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return 1;
            }
            return 0;
        });

        //calcula el % ranking de cada cromosoma y lo guarda en rank
        for (int i = 1; i <= individuals.length; i++) {
            if (totalranking != 0) {
                ranks[i - 1] = (double) i / totalranking * 100;
            } else {
                ranks[i - 1] = 0;
            }
        }

        //calcula valor aleatorio entre 0 y 100
        int randNum = new Random().nextInt(100);
        int partialSum = 0;
        int index = 0;
        while (index < individuals.length - 1 && partialSum < randNum) {
            partialSum += (int) ranks[index];
            index++;
        }
        return copy[index];
    }

    /**
     * N individuals in the population are randomly selected (all with the same probability). From each couple
     * Select the one with the highest fitness. Finally, the N finalists are compared and
     * select the one with the highest fitness. This method tends to generate a distribution of the
     * probability of selection more balanced than the previous N.
     *
     * @param tournamentSize
     * @return
     */
    public Individual selectParentByTournament(int tournamentSize) {
        // Shuffle the individuals to randomize the tournament selection
        shuffle();

        // Select the first 'tournamentSize' individuals from the shuffled list
        Individual[] tournament = Arrays.copyOfRange(individuals, 0, tournamentSize);

        Arrays.sort(tournament, (o1, o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return 1;
            }
            return 0;
        });

        return tournament[0];
    }

    /**
     * Random selections of individuals are made, having first discarded the individuals with the lowest fitness from the population.
     * @param n
     * @return The selected individual.
     */

    public Individual selectParentByTruncation(int n) {
        Individual[] truncation = Arrays.copyOfRange(individuals, 0, individuals.length);
        Arrays.sort(truncation, (o1, o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return 1;
            }
            return 0;
        });
        // Select a random individual discarding the n worst
        int randomPoint = new Random().nextInt(individuals.length - n);
        return truncation[randomPoint];
    }

    /**
     * Performs one-point crossover between two parent individuals.
     *
     * @param parentA The first parent individual.
     * @param parentB The second parent individual.
     * @return An array of offspring individuals resulting from the crossover.
     */
    public Individual[] onePointCrossover(Individual parentA, Individual parentB) {
        Individual[] newIndividuals = new Individual[2];
        newIndividuals[0] = new Individual(parentA.getChromosome().length);
        newIndividuals[1] = new Individual(parentA.getChromosome().length);

        int randPoint = new Random().nextInt(parentA.getChromosome().length);
        int i;
        for (i = 0; i < randPoint; ++i) {
            newIndividuals[0].setGene(i, parentA.getGene(i));
            newIndividuals[1].setGene(i, parentB.getGene(i));
        }
        while (i < parentA.getChromosome().length) {
            newIndividuals[0].setGene(i, parentB.getGene(i));
            newIndividuals[1].setGene(i, parentA.getGene(i));
            i++;
        }

        return newIndividuals;
    }

    /**
     * Performs n-point crossover between two parent individuals.
     *
     * @param parentA The first parent individual.
     * @param parentB The second parent individual.
     * @param n       The number of crossover points.
     * @return An array of offspring individuals resulting from the crossover.
     */
    public Individual[] nPointCrossover(Individual parentA, Individual parentB, int n) {
        int[] randomPoints = new int[n];
        Individual[] newIndividuals = new Individual[2];
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            randomPoints[i] = new Random().nextInt(parentA.getChromosome().length);
        }

        Arrays.sort(randomPoints);

        newIndividuals[0] = new Individual(parentA.getChromosome().length);
        newIndividuals[1] = new Individual(parentA.getChromosome().length);

        for (i = 0; i < n; i++) {
            if (i % 2 == 0) {
                while (j < randomPoints[i]) {
                    newIndividuals[0].setGene(j, parentA.getGene(j));
                    newIndividuals[1].setGene(j, parentB.getGene(j));
                    j++;
                }
            } else {
                while (j < randomPoints[i]) {
                    newIndividuals[0].setGene(j, parentB.getGene(j));
                    newIndividuals[1].setGene(j, parentA.getGene(j));
                    j++;
                }
            }
        }
        return newIndividuals;
    }

    /**
     * Performs uniform crossover between two parent individuals.
     *
     * @param parentA The first parent individual.
     * @param parentB The second parent individual.
     * @return An array of offspring individuals resulting from the crossover.
     */
    public Individual[] uniformCrossover(Individual parentA, Individual parentB) {
        Individual[] newIndividuals = new Individual[2];
        newIndividuals[0] = new Individual(parentA.getChromosome().length);
        newIndividuals[1] = new Individual(parentA.getChromosome().length);

        for (int i = 0; i < parentA.getChromosome().length; i++) {
            if (Math.random() < 0.5) {
                newIndividuals[0].setGene(i, parentA.getGene(i));
                newIndividuals[1].setGene(i, parentB.getGene(i));
            } else {
                newIndividuals[0].setGene(i, parentB.getGene(i));
                newIndividuals[1].setGene(i, parentA.getGene(i));
            }
        }
        return newIndividuals;
    }

    /**
     * Evaluates the fitness of the entire population based on the provided schedule.
     *
     * @param schedule The schedule used for fitness calculation.
     */
    public void evalPopulation(Schedule schedule) {
        double fitness = 0;
        for (Individual individual : individuals) {
            fitness += individual.calcFitness(schedule);
        }
        this.fitness = fitness;
    }

    /**
     * Gets the fitness value of the population.
     *
     * @return The fitness value of the population.
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Gets the size of the population.
     *
     * @return The size of the population.
     */
    public int size() {
        return this.individuals.length;
    }

    /**
     * Sets an individual at the specified offset in the population.
     *
     * @param offset     The offset where the individual is to be set.
     * @param individual The individual to be set.
     */
    public void setIndividual(int offset, Individual individual) {
        individuals[offset] = individual;
    }

    /**
     * Gets an individual at the specified offset in the population.
     *
     * @param offset The offset indicating the position of the individual.
     * @return The individual at the specified offset.
     */
    public Individual getIndividual(int offset) {
        return individuals[offset];
    }

    /**
     * Shuffles the individuals in the population to randomize their order.
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = individuals.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = individuals[index];
            individuals[index] = individuals[i];
            individuals[i] = a;
        }
    }

}