package es.unex.cum.si.practica.model.genotype;

import es.unex.cum.si.practica.model.phenotype.Group;
import es.unex.cum.si.practica.model.phenotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Random;
/**
 * The Individual class stores the chromosome and fitness of an individual.
 * The chromosome is represented as an array of integers. Each gene in the chromosome represents a constraint.
 * In this case, the genes in the even positions represent the room and the genes in the odd positions represent the time.
 * The combination of these two genes represents a class in a given timeslot and room.
 */
public class Individual {
    private final int[] chromosome;
    private double fitness = -1;

    /**
     * Constructs a new individual with a random chromosome based on the provided data.
     *
     * @param data The data containing information about groups, timeslots, and rooms.
     */

    public Individual(Data data){
        int numClasses = Data.getInstance().getNumOfClasses();
        //2 chromosome for each class: 1 gene for room, 1 for time
        int chromosomeLength = numClasses * 2;
        // Create random individual
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;
        // Loop through groups
        for (Group group : data.getGroups().values()) {
            // Loop through subjects
            for (int i = 0; i < group.subjectIds().length ; i++) {
                newChromosome[chromosomeIndex++] = data.getRandomTimeslot().id(); // time for first class
                newChromosome[chromosomeIndex++] = data.getRandomRoom().id(); // room for first class
                newChromosome[chromosomeIndex++] = data.getRandomTimeslot().id(); // time for second class
                newChromosome[chromosomeIndex++] = data.getRandomRoom().id(); // room for second class
            }
        }

        this.chromosome = newChromosome;
    }
    /**
     * Constructs a new individual with an empty chromosome.
     *
     * @param chromosomeLength The length of the chromosome.
     */
    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
    }

    /**
     * Gets the chromosome of the individual.
     *
     * @return The chromosome array.
     */
    public int[] getChromosome() {
        return chromosome;
    }

    /**
     * Sets the gene at the specified offset in the chromosome.
     *
     * @param offset The index where the gene is to be set.
     * @param gene   The value of the gene.
     */
    public void setGene(int offset, int gene) {
        chromosome[offset] = gene;
    }

    /**
     * Calculates the fitness of the individual based on the provided schedule.
     *
     * @param schedule The schedule used for fitness calculation.
     * @return The calculated fitness value.
     */
    public double calcFitness(Schedule schedule) {
        Schedule copy = new Schedule(schedule);
        copy.parseChromosome(this);

        int conflicts = copy.calcConflicts();
        int penalties = copy.calcPenalties();
        int k = 20;
        double fitness = 1 / ((double) ((conflicts + 1) * k + penalties));

        this.fitness = fitness;

        return fitness;
    }

    /**
     * Gets the fitness value of the individual.
     *
     * @return The fitness value.
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Converts the individual to a string representation.
     *
     * @return A string representation of the individual's chromosome.
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i : chromosome) {
            output.append(i).append(",");
        }
        return output.toString();
    }
    /**
     * Gets the gene value at the specified index in the chromosome.
     *
     * @param geneIndex The index of the gene.
     * @return The value of the gene at the specified index.
     */
    public int getGene(int geneIndex) {
        return chromosome[geneIndex];
    }

    /**
     * Applies mutation to the individual's chromosome with the given mutation rate.
     * This mutation involves swapping a gene with a random gene from a random individual.
     *
     * @param rate The mutation rate.
     */
    public void mutate(double rate) {
        Individual randomIndividual = new Individual(Data.getInstance());
        for (int i = 0; i < chromosome.length; i++) {
            if (rate > Math.random()){
                chromosome[i] = randomIndividual.getGene(i);
            }
        }
    }

    /**
     * Applies a custom type of mutation to the individual's chromosome.
     * This mutation involves reversing a portion of the chromosome from a random point to the end.
     * The random point must be odd.
     */
    public void mutate2() {
        Random random = new Random();
        int cutPoint = (random.nextInt(chromosome.length / 2) * 2) + 1;

        for (int i = cutPoint, j = chromosome.length - 1; i < j; i++, j--) {
            int temp = chromosome[i];
            chromosome[i] = chromosome[j];
            chromosome[j] = temp;
        }
    }
}
