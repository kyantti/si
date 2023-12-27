package es.unex.cum.si.maxone;

import java.util.Random;

public class Population {
	int ELITISM_K;
	int POP_SIZE; // population size
	int MAX_ITER; // max number of iterations
	double MUTATION_RATE; // probability of mutation
	double CROSSOVER_RATE; // probability of crossover

	private static Random m_rand = new Random(); // random-number generator
	private Individual[] m_population;
	private double totalFitness;

	public Population(int _ELITISM_K, int _POP_SIZE, int _MAX_ITER,
			double _MUTATION_RATE, double _CROSSOVER_RATE) {
		this.ELITISM_K = _ELITISM_K;
		this.POP_SIZE = _POP_SIZE;
		this.MAX_ITER = _MAX_ITER;
		this.MUTATION_RATE = _MUTATION_RATE;
		this.CROSSOVER_RATE = _CROSSOVER_RATE;

		m_population = new Individual[POP_SIZE];

		// init population
		for (int i = 0; i < POP_SIZE; i++) {
			m_population[i] = new Individual();
			m_population[i].randGenes();
		}

		// evaluate current population
		this.evaluate();
	}

	public void setPopulation(Individual[] newPop) {
		// this.m_population = newPop;
		System.arraycopy(newPop, 0, this.m_population, 0, POP_SIZE);
	}

	public double gettotalFirness() {
		return this.totalFitness;
	}

	public double getm_rand() {
		return this.m_rand.nextDouble();
	}

	public Individual[] getPopulation() {
		return this.m_population;
	}

	public double evaluate() {
		this.totalFitness = 0.0;
		for (int i = 0; i < POP_SIZE; i++) {
			this.totalFitness += m_population[i].evaluate();
		}
		return this.totalFitness;
	}

	public Individual rouletteWheelSelection() {
		double randNum = m_rand.nextDouble() * this.totalFitness;
		int idx;
		for (idx = 0; idx < POP_SIZE && randNum > 0; ++idx) {
			randNum -= m_population[idx].getFitnessValue();
		}
		return m_population[idx - 1];
	}

	public Individual findBestIndividual() {
		int idxMax = 0, idxMin = 0;
		double currentMax = 0.0;
		double currentMin = 1.0;
		double currentVal;

		for (int idx = 0; idx < POP_SIZE; ++idx) {
			currentVal = m_population[idx].getFitnessValue();
			if (currentMax < currentMin) {
				currentMax = currentMin = currentVal;
				idxMax = idxMin = idx;
			}
			if (currentVal > currentMax) {
				currentMax = currentVal;
				idxMax = idx;
			}
			if (currentVal < currentMin) {
				currentMin = currentVal;
				idxMin = idx;
			}
		}

		// return m_population[idxMin]; // minimization
		return m_population[idxMax]; // maximization
	}

	public static Individual[] crossover(Individual indiv1, Individual indiv2) {
		Individual[] newIndiv = new Individual[2];
		newIndiv[0] = new Individual();
		newIndiv[1] = new Individual();

		int randPoint = m_rand.nextInt(Individual.SIZE);
		int i;
		for (i = 0; i < randPoint; ++i) {
			newIndiv[0].setGene(i, indiv1.getGene(i));
			newIndiv[1].setGene(i, indiv2.getGene(i));
		}
		for (; i < Individual.SIZE; ++i) {
			newIndiv[0].setGene(i, indiv2.getGene(i));
			newIndiv[1].setGene(i, indiv1.getGene(i));
		}

		return newIndiv;
	}
}
