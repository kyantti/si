package es.unex.cum.si.practica.model.fenotype;


import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Arrays;
import java.util.Random;

public class Population {
    private final Individual[] individuals;
    private double fitness = -1;

    public Population(int populationSize) {
        // Initial individuals
        this.individuals = new Individual[populationSize];
    }

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

    public Individual selectParentByRouletteWheel(){
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

    public void selectParentByRank(){
        //TODO
    }

    public void selectParentByTruncation(){
        //TODO
    }

    public Individual onePointCrossover(Individual parentA, Individual parentB){
        Individual offspring = new Individual(parentA.getChromosome().length);
        int randomPoint = new Random().nextInt(parentA.getChromosome().length);

        for (int i = 0; i < parentA.getChromosome().length; i++) {
            if (i < randomPoint) {
                offspring.setGene(i, parentA.getGene(i));
            } else {
                offspring.setGene(i, parentB.getGene(i));
            }
        }
        return offspring;
    }

    /*public Individual nPointCrossover(Individual parentA, Individual parentB, int n){
        Individual offspring = new Individual(parentA.getChromosome().length);
        int[] randomPoints = new int[n];
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            randomPoints[i] = new Random().nextInt(parentA.getChromosome().length);
        }

        Arrays.sort(randomPoints);

        for (i = 0; i < n; i++){
            if (i % 2 == 0){
                for (j = randomPoints[i]; j < randomPoints[i+1]; j++){
                    offspring.setGene(j, parentA.getGene(j));
                }
            } else {
                for (j = randomPoints[i]; j < randomPoints[i+1]; j++){
                    offspring.setGene(j, parentB.getGene(j));
                }
            }
        }
        return offspring;
    }*/

    public Individual uniformCrossover(Individual parentA, Individual parentB){
        Individual offspring = new Individual(parentA.getChromosome().length);
        for (int i = 0; i < parentA.getChromosome().length; i++) {
            if (Math.random() < 0.5) {
                offspring.setGene(i, parentA.getGene(i));
            } else {
                offspring.setGene(i, parentB.getGene(i));
            }
        }
        return offspring;
    }

    public Individual[] crossover(Individual indiv1, Individual indiv2) {
        Individual[] newIndiv = new Individual[2];
        newIndiv[0] = new Individual(indiv1.getChromosome().length);
        newIndiv[1] = new Individual(indiv1.getChromosome().length);

        int randPoint = new Random().nextInt(indiv1.getChromosome().length);
        int i;
        for (i = 0; i < randPoint; ++i) {
            newIndiv[0].setGene(i, indiv1.getGene(i));
            newIndiv[1].setGene(i, indiv2.getGene(i));
        }
        for (; i < indiv1.getChromosome().length; ++i) {
            newIndiv[0].setGene(i, indiv2.getGene(i));
            newIndiv[1].setGene(i, indiv1.getGene(i));
        }

        return newIndiv;
    }

    public void evalPopulation(Schedule schedule) {
        for (Individual individual : individuals) {
            fitness += individual.calcFitness(schedule);
        }
    }

    public int size() {
        return this.individuals.length;
    }

    public void setIndividual(int offset, Individual individual) {
        individuals[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return individuals[offset];
    }

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