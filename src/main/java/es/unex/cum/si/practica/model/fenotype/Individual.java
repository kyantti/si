package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.util.Data;

public class Individual {
    private int[] chromosome;
    private double fitness = -1;

    public Individual(Data data){
        int numClasses = data.getSubjects().size() * 2;
        //2 genes for each class: 1 gene for room, 1 for time
        int chromosomeLength = numClasses * 2;
        // Create random individual
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;
        int timeslotId = 0;
        int roomId = 0;
        // Loop through groups
        for (Group group : data.getGroups().values()) {
            // Loop through modules
            for (int moduleId : group.getSubjectIds()) {
                // Add random time for first class (gene 1)
                timeslotId = data.getRandomTimeslot().getId();
                newChromosome[chromosomeIndex] = timeslotId;
                chromosomeIndex++;

                // Add random room for first class (gene 2)
                roomId = data.getRandomRoom().getId();
                newChromosome[chromosomeIndex] = roomId;
                chromosomeIndex++;

                // Add random time for second class (gene 3)
                timeslotId = data.getRandomTimeslot().getId();
                newChromosome[chromosomeIndex] = timeslotId;
                chromosomeIndex++;

                // Add random room for second class (gene 4)
                roomId = data.getRandomRoom().getId();
                newChromosome[chromosomeIndex] = roomId;
                chromosomeIndex++;
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
