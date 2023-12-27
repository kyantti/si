package es.unex.cum.si.maxone;

public class MaxOne {

	@SuppressWarnings({ "static-access", "unused" })
	public static void main(String[] args) {

		int ELITISM_K = 1;  //0 sin elimisto 1 con elitimos puro, solo pasa el primero
		int POP_SIZE = 40 + ELITISM_K; // population size
		int MAX_ITER = 5000; // max number of iterations
		double MUTATION_RATE = 0.1; // probability of mutation
		double CROSSOVER_RATE = 0.7; // probability of crossover

		Population pop = new Population(ELITISM_K, POP_SIZE, MAX_ITER, MUTATION_RATE, CROSSOVER_RATE);
		Individual[] newPop = new Individual[POP_SIZE];
		Individual[] indiv = new Individual[2];

		// current population
		System.out.println("Iteration: 0");
		System.out.print("Total Fitness = " + pop.gettotalFirness());
		System.out.println(" ; Best Fitness = " + pop.findBestIndividual().getFitnessValue());
		System.out.print("Best Individual: ");
		pop.findBestIndividual().show_Individual();

		// main loop

		/*
		 * Ejemplo basado en reemplazamiento generacional
		 */

		int count;
		int iter=0;
		for (iter = 0; iter < MAX_ITER; iter++) {
			count = 0;

			// Elitism
            /*
             * Aplicamos elitismo y pasamos a la nueva población el elitisma
             * Elitismo puro, sólo para el primero
             */
            newPop[count] = pop.findBestIndividual();
            count++;

            // build new Population
			while (count < POP_SIZE) {
				// Selection

				/*
				 * Seleccionamos dos padres por ruleta
				 */
				indiv[0] = pop.rouletteWheelSelection();
				indiv[1] = pop.rouletteWheelSelection();

				// Crossover

				/*
				 * Aplicamos probabilidad de cruce, para ver si hay cruce. En caso de haber
				 * cruze, los padres serán sustituidos por los hijos En caso contrario, los
				 * padres sobreviven.
				 */
				if (pop.getm_rand() < CROSSOVER_RATE) {
					indiv = pop.crossover(indiv[0], indiv[1]);

					// Mutation
					/*
					 * Aplicamos la probabilidad de mutación, para ver si se da mutación en los
					 * individuos Estos individuos serán los que se han generado al producir el
					 * cruce (hijos) o no (padres) Si se da que hay mutción, la mutación coedificada
					 * el flip (cambio de bit) en uno de los bits de forma aleatoria
					 */
					if (pop.getm_rand() < MUTATION_RATE) {
						indiv[0].mutate();
					}
					if (pop.getm_rand() < MUTATION_RATE) {
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
				newPop[count] = indiv[0];
				newPop[count + 1] = indiv[1];
				/*
				 * Se debe aumentar el contador indicando que hay dos individuos más en la nueva
				 * población Se parará este bucle cuando la nueva población llegue al número de
				 * individuos marcados por el usuario al comienzo de la confifuración.
				 */
				count += 2;
			}

			/*
			 * Modelo generacional, cambio total de la población. Se ha incluido el elitismo
			 * y el resultado del cruce y la mutación.
			 */
			pop.setPopulation(newPop);

			// reevaluate current population

			/*
			 * Se evalua la nueva población, ya que es necesario para la siguiente selección
			 * por ruleta.
			 */
			pop.evaluate();
			System.out.println("Iteration: " + iter);
			System.out.print("Total Fitness = " + pop.gettotalFirness());
			System.out.println(" ; Best Fitness = " + pop.findBestIndividual().getFitnessValue());
			System.out.print("Best Individual: ");
			pop.findBestIndividual().show_Individual();
		}

		// best indiv
		/*
		 * Parada-> se muestra el mejor individuo.
		 */
		Individual bestIndiv = pop.findBestIndividual();

		System.out.print("Best Final Individual: ");
		pop.findBestIndividual().show_Individual();

		System.out.println("Done!!");
	}

}
