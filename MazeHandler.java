/*
 "The Only Easy Day Was Yesterday"-Us seal teams
 "It Pays to be a Winner"-US seal teams
 "The generation of random numbers is too important to be left to chance."
 -Robert coveyou
 */
package zombieMaze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class generates the maze implements a simple depth first recursive
 * backtracker
 *
 * @author rohan
 */
public class MazeHandler {

	/**
	 * if the size is even the maze will not be nicely contained by walls Also the
	 * "glade" in the middle will be off center. Not a big deal because the program
	 * automatically renders positions off the side of the maze as walls
	 * recommended size 31
	 */
	public static int size = 16;

	/**
	 * THe size along the y dimension
	 */
	public static int ySize = size;
	/**
	 * The size of the x dimension
	 */
	public static int xSize = size;
	/**
	 * The dimensions are by default all the same. I.e the maze is a square
	 */
	/**
	 * start position for the human and the maze generator
	 */
	public static int[] position = {0, 0, 1, 1};
	/**
	 * 2d maze 1 is an open space 0 is a wall (they all start as walls)
	 */
	public static int[][] maze = new int[ySize][xSize];
	/**
	 * the nodes for use in the algorithm to make a perfect maze (theoretically)
	 * there is a odd bug somewhere here that leaves a loop note in the end this is
	 * irrelevant as we poke holes (rectangles of arbitrary width and height
		* in the maze anyway A place for the program to
	 * store the places its carved passages to This way it can recursively
	 * backtrack to them when needed
	 */
	private static ArrayList<int[]> nodes = new ArrayList(1);

	public static boolean[][] isDungeon = new boolean[size][size];

	/**
	 * turns the int[][] maze into a 2d maze with a recursively backtracking
	 * algorithm that carves passages by setting the value of the index =1 1=hole
	 * 0= wall (optimized to prevent me from having to manually fill in walls)
	 *
	 * @return a int[][] of mazes
	 */
	public static int[][] make2dMaze() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				maze[i][j] = 1;
			}
		}
		int attempts =(int) ((int) Math.pow(size/2.1, 2.0)/20.0);
		for (int i = 0; i < attempts; i++) {
			tryToMakeDungeon();
		}
		//maze = new int[ySize][xSize];
		position = new int[4];
		position[2] = 5;
		position[3] = 5;
		int[] mazeCoords = position.clone();
		nodes.add(0, mazeCoords);
				while (nodes.size() > 0) {
			findNewNode(nodes.get(nodes.size() - 1));
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				maze[i][j] = maze[i][j] == 0 && !isDungeon[i][j] ? 0 : 1;
			}
		}
		return maze.clone();
	}

	/**
	 * finds the next non visited node in the maze
	 *
	 * @param node from which its looking for nodes to grow to if it fails it
	 * deletes this node from the list of nodes its growing to otherwise it adds
	 * the first random node it finds
	 */
	public static void findNewNode(int[] node) {
		boolean done = false;
		int counter = 0;
		int choice;
		int imperfectness = 3;//on a scale of 0 to 19
		boolean shouldGoThroughAnyway = Utility.getRandom(20) <= imperfectness;

		while (!done) {
			if (counter >= 12) {
				choice = counter - 12;
			} else {
				choice = Utility.getRandom(4) + 4;
			}
			try {
				if (choice == 4) {

					if (maze[node[2] + 2][node[3]] == 0 || shouldGoThroughAnyway) {
						maze[node[2] + 2][node[3]] = 1;
						maze[node[2] + 1][node[3]] = 1;
						node[2] += 2;
						done = true;
					}
				} else if (choice == 5) {
					if (maze[node[2] - 2][node[3]] == 0 || shouldGoThroughAnyway) {
						maze[node[2] - 2][node[3]] = 1;
						maze[node[2] - 1][node[3]] = 1;
						node[2] -= 2;
						done = true;
					}
				} else if (choice == 6) {
					if (maze[node[2]][node[3] + 2] == 0 || shouldGoThroughAnyway) {
						maze[node[2]][node[3] + 2] = 1;
						maze[node[2]][node[3] + 1] = 1;
						node[3] += 2;
						done = true;
					}
				} else if (choice == 7) {
					if (maze[node[2]][node[3] - 2] == 0 || shouldGoThroughAnyway) {
						maze[node[2]][node[3] - 2] = 1;
						maze[node[2]][node[3] - 1] = 1;
						node[3] -= 2;
						done = true;
					}
				} else if (choice == 8) {//wait a second there are no choices left
					nodes.remove(nodes.size() - 1);
					return;
				}

			} catch (Exception e) {/*gotcha*/
//cant afford to go off the map now can we

			}
			counter++;
		}
		int[] cloned = node.clone();
		nodes.add(cloned);

	}

	/**
	 * makes the little holes in the maze Almost like the glade in maze runner By
	 * glade radius it means the farthest distance away from the center cube the
	 * glade extends counting diagonals as only one unit of distance (we use
	 * neither pythagoras nor taxicab norms)
	 *
	 * @param centerX
	 * @param centerY
	 */
	public static void makeGlade(int centerX, int centerY) {
		int d = size / 20 + 1;
		for (int y = centerY - d; y <= centerY + d; y++) {
			for (int x = centerX - d; x <= centerX + d; x++) {
				try {
					if (Math.abs(x - centerX) == d || Math.abs(y - centerY) == d) {

					} else {
						maze[y][x] = 1;
					}
				} catch (Exception e) {
					/**
					 * gotcha
					 */
				}
			}
		}

	}

	private static void tryToMakeDungeon() {
		int xCoords = Utility.getRandom(size - 14) + 7;
		int yCoords = Utility.getRandom(size - 14) + 7;
		int height = Utility.getRandom(4) + 3;
		int width = Utility.getRandom(4) + 3;
		boolean shouldCarve = true;
		int space = 3;//how far apart the dungeons are
		for (int i = xCoords - space; i < xCoords + width; i++) {
			for (int j = yCoords - space; j < height + yCoords - space; j++) {
				if (maze[i][j] == 1) {
					return;
				}
			}
		}

			for (int i = xCoords; i < xCoords + width; i++) {
				for (int j = yCoords; j < height + yCoords; j++) {
					maze[i][j] = 1;
				}
			}
		}
}
