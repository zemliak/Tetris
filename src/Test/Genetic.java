package Test;

import java.util.*;
import Test.Evaluate;
import tetris.AI1;



public class Genetic {
	private static double average(ScorePair[] a, int n) {
		double average = 0.0;
		for (int i = 0; i < n; i++) {
			average += a[i].fitness / n;
		}
		return average;
	}

	public static void main(String[] args) {
		Genetic gen = new Genetic(10, 20);
		long lastTime = System.nanoTime();
		int i = 0;
		while (i<3) {
			gen.evolve();
			System.out.println(gen.generation + ": "
					+ average(gen.population, gen.breedSize));
			System.out.println(Arrays.toString(gen.population));
			System.out.println(Arrays.toString(gen.population[0].c)
					.replace('[', '{').replace(']', '}'));
			long time = System.nanoTime();
			System.out.println("time: " + (time - lastTime) / 1.0e9 + " s");
			lastTime = time;
			System.out.println();
			++i;
		}
	}

	private int breedSize = 2;

	private Random rand = new Random();
	private int generation = 0;

	private Evaluate eval;
	private ScorePair[] population;

	public Genetic(int gridHeight, int gridWidth) {
		this.eval = new Evaluate(gridHeight, gridWidth);
		population = new ScorePair[breedSize * (breedSize + 1) / 2 + breedSize];
		for (int i = 0; i < population.length; i++) {
			double[] c = new double[11];
			population[i] = new ScorePair(c, 0);
		}
	}

	public int getGeneration() {
		return this.generation;
	}

	public void evolve() {
		ScorePair[] fittest = new ScorePair[breedSize];
		System.arraycopy(population, 0, fittest, 0, breedSize);

		int i = 0;
		while (i < breedSize) {
			double[] child = population[i].c;
			double fitness = getFitness(child);
			population[i++] = new ScorePair(child, fitness);
		}
		for (int p = 0; p < fittest.length; p++) {
			double[] child = fittest[p].c.clone();
			mutate(child);
			double fitness = getFitness(child);
			population[i++] = new ScorePair(child, fitness);
		}
		for (int p1 = 0; p1 < fittest.length; p1++) {
			for (int p2 = p1 + 1; p2 < fittest.length; p2++) {
				double[] child = breed(fittest[p1].c, fittest[p2].c);
				double fitness = getFitness(child);
				population[i++] = new ScorePair(child, fitness);
			}
		}
		Arrays.sort(population, Collections.reverseOrder());

		generation++;
	}

	private double[] breed(double[] p1, double[] p2) {
		double[] child = new double[11];
		for (int i = 0; i < child.length; i++) {
			child[i] = p1[i] * 0.5 + p2[i] * 0.5;
		}
		mutate(child);
		return child;
	}

	private void mutate(double[] c) {
		for (int i = 0; i < c.length; i++) {
			c[i] += (double) (rand.nextInt(10000) - 5000) / 1000.0;
		}
	}

	private double getFitness(double[] c) {
		AI1 ai = new AI1(c);
		return eval.evalN(ai, 1, 1);
	}

	private static class ScorePair implements Comparable<ScorePair> {
		double[] c;
		double fitness;

		public ScorePair(double[] c, double fitness) {
			this.c = c;
			this.fitness = fitness;
		}

		@Override
		public int compareTo(ScorePair other) {
			return Double.compare(this.fitness, other.fitness);
		}

		@Override
		public String toString() {
			return "" + fitness;
		}
	}
}

