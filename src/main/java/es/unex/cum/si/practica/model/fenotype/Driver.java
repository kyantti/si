package es.unex.cum.si.practica.model.fenotype;

import es.unex.cum.si.practica.model.genotype.Class;
import es.unex.cum.si.practica.model.genotype.Schedule;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Random;

public class Driver {
    static int ELITISM_K = 1;  //0 sin elimisto 1 con elitimos puro, solo pasa el primero
    static int POP_SIZE = 50 + ELITISM_K; // population size
    static int MAX_ITER = 500; // max number of iterations
    static double MUTATION_RATE = 0.01; // probability of mutation
    static double CROSSOVER_RATE = 0.8; // probability of crossover
    private static Random random = new Random();
    public static void main(String[] args) {

        Schedule schedule = new Schedule(Data.getInstance().getNumOfClasses());
        Population pop = new Population(POP_SIZE, Data.getInstance());
        //Population newPop = new Population(POP_SIZE);
        Individual[] indiv = new Individual[2];
        int i = 0;

        // first evaluation
        pop.evalPopulation(schedule);

        while (i < MAX_ITER &&  pop.getFittest(0).getFitness() != 1){

            // Print fitness
            System.out.println("Generation: " + i + ", Best fitness: " + pop.getFittest(0).getFitness());

            Population newPopulation = new Population(pop.size());

            int j = 0;

            // Elitism
            if (ELITISM_K > 0) {
                for (j = 0; j < ELITISM_K; j++) {
                    newPopulation.setIndividual(j, pop.getFittest(j));
                }
            }

            // build new Population
            while (j < POP_SIZE) {
                // Selection

                /*
                 * Seleccionamos dos padres por ruleta
                 */
                indiv[0] = pop.selectParentByRouletteWheel();
                indiv[1] = pop.selectParentByRouletteWheel();

                // Crossover

                /*
                 * Aplicamos probabilidad de cruce, para ver si hay cruce. En caso de haber
                 * cruze, los padres serán sustituidos por los hijos En caso contrario, los
                 * padres sobreviven.
                 */
                if (random.nextDouble() < CROSSOVER_RATE) {
                    indiv = pop.crossover(indiv[0], indiv[1]);

                    // Mutation
                    /*
                     * Aplicamos la probabilidad de mutación, para ver si se da mutación en los
                     * individuos Estos individuos serán los que se han generado al producir el
                     * cruce (hijos) o no (padres) Si se da que hay mutción, la mutación coedificada
                     * el flip (cambio de bit) en uno de los bits de forma aleatoria
                     */
                    if (random.nextDouble() < MUTATION_RATE) {
                        indiv[0].mutate();
                    }
                    if (random.nextDouble() < MUTATION_RATE) {
                        indiv[1].mutate();
                    }
                }

                // add to new population
                /*
                 * Se añaden los dos nuevos individuos a la nueva población. Al ser
                 * generacional, se sustituirán los individuos seleccionados para el cruce y ha
                 * habido cruce o no.
                 *
                 */
                newPopulation.setIndividual(j, indiv[0]);
                newPopulation.setIndividual(j + 1, indiv[1]);
                /*
                 * Se debe aumentar el contador indicando que hay dos individuos más en la nueva
                 * población Se parará este bucle cuando la nueva población llegue al número de
                 * individuos marcados por el usuario al comienzo de la confifuración.
                 */
                j += 2;
            }

            /*
             * Modelo generacional, cambio total de la población. Se ha incluido el elitismo
             * y el resultado del cruce y la mutación.
             */
            pop = newPopulation;

            // reevaluate current population

            /*
             * Se evalua la nueva población, ya que es necesario para la siguiente selección
             * por ruleta.
             */
            pop.evalPopulation(schedule);
            i++;
        }

        // best indiv
        /*
         * Parada-> se muestra el mejor individuo.
         */
        // Print fitness
        schedule.parseChromosome(pop.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + i + " generations");
        System.out.println("Final solution fitness: " + pop.getFittest(0).getFitness());
        System.out.println("Conflicts: " + schedule.calcConflicts());

    }
}
