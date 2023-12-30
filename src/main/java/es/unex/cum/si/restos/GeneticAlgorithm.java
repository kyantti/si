package es.unex.cum.si.restos;

import es.unex.cum.si.practica.model.fenotype.Individual;
import es.unex.cum.si.practica.model.fenotype.Population;
import es.unex.cum.si.practica.model.genotype.Class;
import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

public class GeneticAlgorithm {
    private final int populationSize;
    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;
    protected int tournamentSize;
    private final int maxGenerations;

    public GeneticAlgorithm(int maxGenerations, int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
        this.maxGenerations = maxGenerations;
    }

    private Population initPopulation(Data data) {
        // Initialize population
        return new Population(this.populationSize, data);
    }

    private boolean isTerminationConditionMet(int generationsCount, Population population){
        return (generationsCount > maxGenerations) || (population.getFittest(0).getFitness() == 1.0);
    }

    private void evalPopulation(Population population, Schedule schedule) {
        population.evalPopulation(schedule);
    }

    /*public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.tournamentSize);

        // Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        return tournament.getFittest(0);
    }*/

    private Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int i = 0; i < population.size(); i++) {
            Individual parent1 = population.getFittest(i);

            // Apply crossover to this individual?
            if (crossoverRate > Math.random() && i >= elitismCount) {
                // Find second parent
                Individual parent2 = population.selectParentByRouletteWheel();

                // Initialize offspring
                Individual offspring = new Individual(parent1.getChromosome().length);

                // Loop over genome
                for (int j = 0; j < parent1.getChromosome().length; j++) {
                    // Use half of parent1's genes and half of parent2's genes
                    if (0.5 > Math.random()) {
                        offspring.setGene(j, parent1.getGene(j));
                    } else {
                        offspring.setGene(j, parent2.getGene(j));
                    }
                }

                // Add offspring to new population
                newPopulation.setIndividual(i, offspring);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(i, parent1);
            }
        }

        return newPopulation;
    }

    private Population crossoverPopulation2(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());
        Individual[] offspring;
        int i;
        for (i  = 0; i < elitismCount; i++) {
            newPopulation.setIndividual(i, population.getFittest(i));
        }
        while (i < population.size()) {
            Individual parentA = population.selectParentByRouletteWheel();
            Individual parentB = population.selectParentByRouletteWheel();
            if (crossoverRate > Math.random()) {
                offspring = population.onePointCrossover(parentA, parentB);
                newPopulation.setIndividual(i, offspring[0]);
                i++;
                if (i < population.size()) {
                    newPopulation.setIndividual(i, offspring[1]);
                    i++;
                }
            } else {
                newPopulation.setIndividual(i, parentA);
                i++;
                if (i < population.size()) {
                    newPopulation.setIndividual(i, parentB);
                    i++;
                }
            }
        }

        return newPopulation;
    }

    private Population mutatePopulation(Population population, Data data) {
        // Initialize new population
        Population newPopulation = new Population(this.populationSize);

        // Loop over current population by fitness
        for (int i = 0; i < population.size(); i++) {
            Individual individual = population.getFittest(i);

            // Create random individual to swap genes with


            // Add individual to population
            newPopulation.setIndividual(i, individual);
        }

        // Return mutated population
        return newPopulation;
    }

    private Population mutatePopulation2(Population population){
        Population newPopulation = new Population(population.size());
        Individual individual;
        int i;
        for (i = 0; i < elitismCount; i++) {
            newPopulation.setIndividual(i, population.getFittest(i));
        }
        while (i < population.size()) {
            individual = population.getIndividual(i);
            Individual randomIndividual = new Individual(Data.getInstance());

            // Loop over individual's genes
            for (int j = 0; j < individual.getChromosome().length; j++) {
                // Skip mutation if this is an elite individual
                if (i > this.elitismCount && (this.mutationRate > Math.random())) {
                    // Swap for new gene
                    individual.setGene(j, randomIndividual.getGene(j));
                }
            }
            newPopulation.setIndividual(i, individual);
            i++;
        }
        return newPopulation;
    }



    public Schedule run(){
        // Create schedule object with array of classes
        Schedule schedule = new Schedule(Data.getInstance().getNumOfClasses());

        // Initialize GA
        //GeneticAlgorithm ga = new GeneticAlgorithm(50, 0.01, 0.8, 1, 5);

        // Initialize population, needs data like number of classes, groups, rooms, etc...
        Population population = initPopulation(Data.getInstance());

        // Evaluate population
        evalPopulation(population, schedule);

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (!isTerminationConditionMet(generation, population)) {
            // Print fitness
            System.out.println("Generation: " + generation + ", Best fitness: " + population.getFittest(0).getFitness());

            // Apply crossover
            population = crossoverPopulation2(population);

            // Apply mutation
            population = mutatePopulation2(population);

            // Evaluate population
            evalPopulation(population, schedule);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        schedule.parseChromosome(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());

        /* Print classes
        System.out.println();
        Class[] classes = schedule.getClasses();
        int classIndex = 1;
        for (Class bestClass : classes) {
            System.out.println("Class " + classIndex + ":");
            System.out.println("Module: " +
                    Data.getInstance().getSubject(bestClass.subjectId()).denomination());
            System.out.println("Group: " +
                    Data.getInstance().getGroup(bestClass.groupId()).id());
            System.out.println("Room: " +
                    Data.getInstance().getRoomSlot(bestClass.roomId()).denomination());
            System.out.println("Time: " +
                    Data.getInstance().getTimeSlot(bestClass.timeId()).denomination());
            System.out.println("-----");
            classIndex++;
        }*/

        return schedule;
    }

    public Schedule run2() {
        // Create schedule object with an array of classes
        Schedule schedule = new Schedule(Data.getInstance().getNumOfClasses());

        // Initialize population
        Population population = new Population(this.populationSize, Data.getInstance());

        // Evaluate population
        evalPopulation(population, schedule);

        // Keep track of current generation
        int generation = 1;

        // Start evolution loop
        while (!isTerminationConditionMet(generation, population)) {
            // Print fitness
            System.out.println("Generation: " + generation + ", Best fitness: " + population.getFittest(0).getFitness());

            // Apply crossover
            Population newPopulation = new Population(population.size());
            for (int i = 0; i < elitismCount; i++) {
                newPopulation.setIndividual(i, population.getFittest(i));
            }

            int i = elitismCount;

            while (i < population.size()) {
                Individual parentA = population.selectParentByRouletteWheel();
                Individual parentB = population.selectParentByRouletteWheel();
                if (crossoverRate > Math.random()) {
                    Individual[] offspring = population.onePointCrossover(parentA, parentB);
                    newPopulation.setIndividual(i, offspring[0]);
                    i++;
                    if (i < population.size()) {
                        newPopulation.setIndividual(i, offspring[1]);
                        i++;
                    }
                } else {
                    newPopulation.setIndividual(i, parentA);
                    i++;
                    if (i < population.size()) {
                        newPopulation.setIndividual(i, parentB);
                        i++;
                    }
                }
            }

            // Apply mutation
            for (i = elitismCount; i < newPopulation.size(); i++) {
                Individual individual = newPopulation.getIndividual(i);
                Individual randomIndividual = new Individual(Data.getInstance());

                // Loop over individual's genes
                for (int j = 0; j < individual.getChromosome().length; j++) {
                    // Skip mutation if this is an elite individual
                    if (i > this.elitismCount && (this.mutationRate > Math.random())) {
                        // Swap for a new gene
                        individual.setGene(j, randomIndividual.getGene(j));
                    }
                }
            }

            population = newPopulation;

            // Evaluate population
            population.evalPopulation(schedule);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        schedule.parseChromosome(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());

        return schedule;
    }

    /*public void runMine(){
        // Create schedule object with an array of classes
        Schedule schedule = new Schedule(Data.getInstance().getNumOfClasses());

        // Initialize population
        Population population = new Population(POP_SIZE, Data.getInstance());
        Population newPopulation;
        // Evaluate population
        population.evalPopulation(schedule);

        // Keep track of current generation
        int i = 0;
        int j = 0;

        // Start evolution loop
        while (i < MAX_ITER && population.getFittest(0).getFitness() != 1) {
            // Print fitness
            System.out.println("Generation: " + i + ", Best fitness: " + population.getFittest(0).getFitness() + ", Total fitness: " + population.getFitness());

            // Apply crossover
            newPopulation = new Population(population.size());
            for (j = 0; j < ELITISM_K; j++) {
                newPopulation.setIndividual(j, population.getFittest(j));
            }

            while (j < population.size()) {

                Individual parentA = population.selectParentByRouletteWheel();
                Individual parentB = population.selectParentByRouletteWheel();

                if (CROSSOVER_RATE > Math.random()) {
                    Individual[] offspring = population.onePointCrossover(parentA, parentB);
                    newPopulation.setIndividual(j, offspring[0]);
                    j++;
                    if (j < population.size()) {
                        newPopulation.setIndividual(j, offspring[1]);
                        j++;
                    }
                } else {
                    newPopulation.setIndividual(j, parentA);
                    j++;
                    if (j < population.size()) {
                        newPopulation.setIndividual(j, parentB);
                        j++;
                    }
                }
            }

            // Apply mutation
            for (j = ELITISM_K; j < newPopulation.size(); j++) {
                Individual individual = newPopulation.getIndividual(j);
                individual.mutate(MUTATION_RATE);
            }

            population = newPopulation;

            population.evalPopulation(schedule);

            i++;
        }

        // Print fitness
        schedule.parseChromosome(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + i + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness() + ", Total fitness: " + population.getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());
    }*/

    /*
    public int calcTimeGaps() {
        int timeGaps = 0;
        Class[] sortedClasses = Arrays.copyOf(classes, classes.length);
        Arrays.sort(sortedClasses, Comparator.comparingInt(Class::groupId).thenComparingInt(Class::timeId));

        for (int i = 0; i < sortedClasses.length; i++) {
            for (int j = i + 1; j < sortedClasses.length; j++) {
                if (sortedClasses[i].groupId() == sortedClasses[j].groupId() && (Math.abs(sortedClasses[i].timeId() - sortedClasses[j].timeId()) > 1)) {
                    timeGaps += Math.abs(sortedClasses[i].timeId() - sortedClasses[j].timeId());
                }
            }
        }
        return timeGaps;
    }

    public int calcQuality() {
        int quality = 0;
        // If a group has more than 3 classes in a row, the quality is increased by 1
        //Sort classes by time slot id (lower to higher)
        Class[] aClasses = Arrays.copyOf(classes, classes.length);
        Class[] bClasses = Arrays.copyOf(classes, classes.length);
        Arrays.sort(aClasses, Comparator.comparingInt(Class::timeId));
        Arrays.sort(aClasses, Comparator.comparingInt(Class::groupId));

        //Sort classes by group id (lower to higher)
        Arrays.sort(bClasses, Comparator.comparingInt(Class::groupId));
        Arrays.sort(bClasses, Comparator.comparingInt(Class::groupId));
        for (Class classA : aClasses) {
            int count = 0;
            for (Class classB : bClasses) {
                if (classA.groupId() == classB.groupId() && (Math.abs(classA.timeId() - classB.timeId()) == 1)) {
                    count++;
                    if (count > 3) {
                        quality++;
                    }
                }
            }
        }
        return quality;
    }
    /*public Individual onePointCrossover(Individual parentA, Individual parentB){
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
    }*/

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

    /*public Individual uniformCrossover(Individual parentA, Individual parentB){
        Individual offspring = new Individual(parentA.getChromosome().length);
        for (int i = 0; i < parentA.getChromosome().length; i++) {
            if (Math.random() < 0.5) {
                offspring.setGene(i, parentA.getGene(i));
            } else {
                offspring.setGene(i, parentB.getGene(i));
            }
        }
        return offspring;
    }*/

}
