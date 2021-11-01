

// It can be run in any java ide

/*
Generic algorithm start with POPULATION_SIZE of individual.
Each generation, the POPULATION produce children (number of children is POPULATION_SIZE).
Children are through cross over (REPRODUCE), if the child is not better than parent, mutate child.
Select best (lest distance) POPULATION_SIZE from parents and children 
Reproduce the whole process for NUMBER_GENERATIONS times.
Select the best individual from the POPULATION.
*/
import java.util.Random;
import java.util.ArrayList;

public class Main {
	static Random rand = new Random();
	static final int[][] MATRIX = { {}, { 8 }, { 6, 7 }, { 7, 9, 13 }, { 5, 8, 5, 9 }, { 3, 3, 2, 6, 4 },
			{ 1, 3, 4, 7, 3, 10 }, { 9, 5, 8, 4, 9, 9, 7 }, { 4, 5, 3, 1, 8, 1, 2, 2 },
			{ 2, 11, 5, 3, 6, 4, 8, 9, 5 } };
	static final int POPULATION_SIZE = 500;
	static final int NUMBER_GENERATIONS = 35;
	static final int MUTATION_CHANCE = 3;
	static final int BAD_CITIES[] = { 8, 6, 10, 7, 5, 9, 2, 4, 3, 1 };

	/*
	 * DISTANCE - return distance of 2 cities
	 */
	static int DISTANCE(int x, int y) {
		if (x < y)
			return MATRIX[y - 1][x - 1];
		else
			return MATRIX[x - 1][y - 1];
	}

	/*
	 * SWAP swap 2 number in an array value of 2 positions in array will be swapped
	 */
	static void SWAP(int[] cities, int x, int y) {
		int temp = cities[x];
		cities[x] = cities[y];
		cities[y] = temp;
	}
	/*
	 * GENERATE_INDIVIDUAL - generate a random set of 10 cities it returns an array
	 * with 10 elements, it is a permutation of integer numbers from 1 to 10
	 */

	static int[] GENERATE_INDIVIDUAL() {
		int cities[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		for (int i = 0; i < 9; i++) {
			for (int j = i + 1; j < 10; j++) {
				if (rand.nextBoolean()) {
					SWAP(cities, i, j);
				}

			}
		}
		return cities;
	}

	/*
	 * FITNESS-FN - accepts the individual as an argument and returns it's fitness
	 * value (the total distance).
	 */

	static int FITNESS_FN(int[] cities) {
		int fitness = 0;
		for (int i = 1; i < 10; i++) {
			fitness += DISTANCE(cities[i - 1], cities[i]);
		}
		return fitness;
	}

	static ArrayList<int[]> POPULATION = new ArrayList<int[]>();

	/*
	 * RANDOM-SELECTION - accepts the population. Uses the FITNESS-FN to judge
	 * fitness of the individual Return index of parent and the best fitness return
	 * an integer array with 2 first elements are indexes of parents last element is
	 * best fitness
	 */

	static int[] RANDOM_SELECTION(ArrayList<int[]> POPULATION) {
		int arr[] = new int[2];
		int parent1 = rand.nextInt(POPULATION_SIZE);
		int parent2 = rand.nextInt(POPULATION_SIZE);
		while (parent2 == parent1) {
			parent2 = rand.nextInt(POPULATION_SIZE);
		}
		arr[0] = parent1;
		arr[1] = parent2;
		return arr;
	}

	/*
	 * REPRODUCE - accepts two individual as it's only argument and performs the
	 * crossover. Creating and returning the child individual which gets placed in
	 * the new population. One-point crossover.
	 */

	static int[] REPRODUCE(int x, int y) {
		int crossover_point = rand.nextInt(9);
		int child[] = new int[10];
		for (int i = 0; i <= crossover_point; i++) {
			child[i] = POPULATION.get(x)[i];
		}
		int[] yArr = POPULATION.get(y);
		int yIndex = 0;
		for (int i = crossover_point + 1; i < 10; i++) {
			while (true) {
				boolean flag = true;
				for (int j = 0; j <= crossover_point; j++) {
					if (yArr[yIndex] == child[j]) {
						yIndex++;
						flag = false;
						break;
					}
				}
				if (flag)
					break;
			}
			child[i] = yArr[yIndex];
			yIndex++;
		}
		return child;
	}
	/*
	 * MUTATE - accepts an individual as it's only argument and swap 2 random
	 * cities. use MUTATION_CHANCE to determine of swapping that pair
	 */

	static void MUTATE(int[] cities) {
		int x = rand.nextInt(10);
		int y = rand.nextInt(10);
		while (x == y) {
			y = rand.nextInt(10);
		}
		if (rand.nextInt(100) < MUTATION_CHANCE) {
			SWAP(cities, x, y);
		}
	}

	/*
	 * BEST-POPULATION - Select all best individual from 2*POPULATION_SIZE sets
	 */
	static void BEST_POPULATION() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			int minIndex = i;
			for (int j = i + 1; j < (2 * POPULATION_SIZE); j++) {
				if (FITNESS_FN(POPULATION.get(j)) < FITNESS_FN(POPULATION.get(minIndex)))
					minIndex = j;
				else {
					if (FITNESS_FN(POPULATION.get(j)) == FITNESS_FN(POPULATION.get(minIndex)))
						if (EQUAL(POPULATION.get(j), POPULATION.get(minIndex)))
							POPULATION.set(j, GENERATE_INDIVIDUAL());
						else
							minIndex = j;
				}
			}
			int[] tempArr = new int[10];
			for (int j = 0; j < 10; j++)
				tempArr[j] = POPULATION.get(minIndex)[j];
			POPULATION.set(minIndex, POPULATION.get(i));
			POPULATION.set(i, tempArr);

		}

	}

	/*
	 * EQUAL - accept 2 sets of cities (2 array length 10) return true if 2 sets are
	 * the same false if they are not
	 */
	static boolean EQUAL(int[] x, int[] y) {
		for (int i = 0; i < 9; i++) {
			if (x[i] != y[i])
				return false;
		}
		return true;
	}

	/*
	 * IN_POPULATION - accept a set of cities Return true if that set is already in
	 * POPULATION false if that set is not in POPULATION
	 */
	static boolean IN_POPULATION(int[] cities) {
		for (int i = 0; i < POPULATION.size(); i++) {
			if (EQUAL(cities, POPULATION.get(i)))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		for (int k = 0; k < 10; k++) {
			System.out.println("RUN NUMBER " + (k + 1));

			// Initial population
			for (int i = 0; i < POPULATION_SIZE; i++) {

				int cities[] = GENERATE_INDIVIDUAL();
				while (IN_POPULATION(cities)) {
					cities = GENERATE_INDIVIDUAL();
				}
				POPULATION.add(GENERATE_INDIVIDUAL());
			}

			// Expand the array to 2 size
			for (int i = 0; i < POPULATION_SIZE; i++) {
				POPULATION.add(new int[10]);
			}

			// Go through generations
			for (int i = 0; i < NUMBER_GENERATIONS; i++) {
				for (int j = 0; j < POPULATION_SIZE; j++) {
					int arr[] = RANDOM_SELECTION(POPULATION);
					int parent1 = arr[0];
					int parent2 = arr[1];
					int child[] = REPRODUCE(parent1, parent2);
					MUTATE(child);
					POPULATION.set(POPULATION_SIZE + j, child);
				}

				BEST_POPULATION();
			}
			System.out.println("Initial Population: " + POPULATION_SIZE);
			System.out.println("Number of Generations: " + NUMBER_GENERATIONS);
			System.out.println("Mutation Chance: " + MUTATION_CHANCE + "%");
			System.out.print("Best Solution: ");
			for (int i = 0; i < 10; i++) {
				System.out.print(POPULATION.get(0)[i] + " ");
			}
			System.out.println("\nTotal Distances: " + FITNESS_FN(POPULATION.get(0)) + "\n");
		}
	}
}

/*OUTPUT SCREEN SHOT 
RUN NUMBER 1
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 2 6 3 5 
Total Distances: 22

RUN NUMBER 2
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 3
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 2 6 3 5 
Total Distances: 22

RUN NUMBER 4
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 2 6 3 5 
Total Distances: 22

RUN NUMBER 5
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 6
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 7
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 8
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 9
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 5 3 6 2 
Total Distances: 22

RUN NUMBER 10
Initial Population: 500
Number of Generations: 35
Mutation Chance: 3%
Best Solution: 8 9 4 10 1 7 2 6 3 5 
Total Distances: 22

 */

