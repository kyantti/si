package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.util.Data;

public class Individual {
    private int[] chromosome;
    private double fitness = -1;

    public Individual(Data data){
        int numClasses = Data.getInstance().getNumOfClasses();
        //2 genes for each class: 1 gene for room, 1 for time
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

    public Individual(int[] chromosome) {
        // Create individual chromosome
        this.chromosome = chromosome;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setGene(int offset, int gene) {
        chromosome[offset] = gene;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
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
}
