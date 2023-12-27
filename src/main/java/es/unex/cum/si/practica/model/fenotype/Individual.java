package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Random;

public class Individual {
    private final int[] chromosome;
    private double fitness = -1;

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

    public Individual(int chromosomeLength) {
        // Create random individual
        int[] individual;
        individual = new int[chromosomeLength];
        for (int gene = 0; gene < chromosomeLength; gene++) {
            individual[gene] = gene;
        }

        this.chromosome = individual;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setGene(int offset, int gene) {
        chromosome[offset] = gene;
    }

    /**
     * Calculate individual's fitness value
     *
     * @return fitness
     */
    public double calcFitness(Schedule schedule) {

        // Create new schedule object to use -- cloned from an existing schedule
        Schedule threadSchedule = new Schedule(schedule);
        threadSchedule.parseChromosome(this);

        // Calculate fitness
        int clashes = threadSchedule.calcConflicts();
        //int timeGaps = threadSchedule.calcTimeGaps();
        //int quality = threadSchedule.calcQuality();
        int k = 1000;
        int a = 10;
        double fitness = 1 / ((double) ((clashes + 1)));

        this.fitness = fitness;

        return fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i : chromosome) {
            output.append(i).append(",");
        }
        return output.toString();
    }

    public boolean containsGene(int gene) {
        for (int j : chromosome) {
            if (j == gene) {
                return true;
            }
        }
        return false;
    }

    public int getGene(int geneIndex) {
        return chromosome[geneIndex];
    }

    public void mutate() {
        Individual randomIndividual = new Individual(Data.getInstance());
        Random random = new Random();
        // Loop over individual's genes
        for (int i = 0; i < chromosome.length; i++) {
            if (0.01 > Math.random()){
                // Swap for new gene
                chromosome[i] = randomIndividual.getGene(i);
            }
        }
    }
}
