package zombieMaze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rohan
 */
public class Map implements Runnable {

    public static int[][] pathFind;
    public static char[][] map;
    public static Player p;
    public static int score=0;
    public static ArrayList<Zombie> zombies;
    public static int round;

    private static char[][] drawAMap() {
        int[][] tempMap = MazeHandler.make2dMaze();
        map = new char[tempMap.length][tempMap[0].length];
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[0].length; j++) {
                map[i][j] = tempMap[i][j] == 1 ? 'o' : 'w';
            }
        }
        map[0][0] = 'w';
        return map;
        /*
	String[] tempMaze=file.getWordsFromFile("map.txt");
	map=new char[tempMaze.length][tempMaze[0].length()];
	for(int i=0;i<tempMaze.length;i++){
		map[i]=tempMaze[i].toCharArray();
	}
	return map;
}
         */
    }

    /**
     * Inefficient implementation of a naive pathfinding algorithm starts from
     * the position of the player and "grows" out finding the number of steps to
     * the player They player is one step away from itself its neighbors are two
     * etc. The only squares 0 steps away are walls. This allows the zombies to
     * find their way to the player
     */
    public static void doPathFinding() {
        pathFind = new int[map.length][map[0].length];
        pathFind[p.getX()][p.getY()] = 1;
        int timesRun = map.length * map.length * 3 / 5;//approximately the biggest distance between any two tiles
        int worstCase = timesRun + 1;//worst case path
        int bestDecision = worstCase;//best neighboring tile to go to next
        for (int a = 0; a < timesRun; a++) {
            for (int i = 0; i < pathFind.length; i++) {
                for (int j = 0; j < pathFind[0].length; j++) {
                    if (pathFind[i][j] == 0 && map[i][j] != 'w') {
                        try {
                            if (pathFind[i + 1][j] < bestDecision && pathFind[i + 1][j] != 0) {
                                bestDecision = pathFind[i + 1][j];
                                pathFind[i][j] = bestDecision + 1;
                            }
                        } catch (Exception e) {
                        }
                        try {
                            if (pathFind[i - 1][j] < bestDecision && pathFind[i - 1][j] != 0) {
                                bestDecision = pathFind[i - 1][j];
                                pathFind[i][j] = bestDecision + 1;
                            }
                        } catch (Exception e) {
                        }
                        try {
                            if (pathFind[i][j - 1] < bestDecision && pathFind[i][j - 1] != 0) {
                                bestDecision = pathFind[i][j - 1];
                                pathFind[i][j] = bestDecision + 1;
                            }
                        } catch (Exception e) {
                        }
                        try {
                            if (pathFind[i][j + 1] < bestDecision && pathFind[i][j + 1] != 0) {
                                bestDecision = pathFind[i][j + 1];
                                pathFind[i][j] = bestDecision + 1;
                            }
                        } catch (Exception e) {
                        }
                        bestDecision = worstCase;
                    }
                }
            }
        }
    }

    public Map() {
        if (zombies != null) {
            for (Zombie z : zombies) {
                if (z != null) {
                    z.die();
                }
            }
        }
        Map.score=0; 
        p = new Player();
        drawAMap();
        doPathFinding();
        Laser.range = 5;
        Laser.level = 1;
        round = 0;
    }

    @Override
    /**
     * Spawns various items on the map each round it follows certain hardcoded
     * presets for adaptive difficulty one could fiddle with these settings to
     * change the difficulty
     */
    public void run() {
        if (zombies != null) {
            for (Zombie z : zombies) {
                if (z != null) {
                    z.die();
                }
            }
        }
        zombies = null;
        zombies = new ArrayList(1);
        round = 0;
        while (p.alive) {
            while (areZombies()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(round!=0){
                score+=50+round*5;
            }
            round++;
            try {
                Thread.sleep(1000 + 4000 / round);
            } catch (InterruptedException ex) {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            }
            int difficulty = (int) Math.sqrt(round + 9)+1 + (int) (round);
            int zDist = 4 + (int) (Math.sqrt(round) + Utility.getRandom(2));//minimum distance for zombies spawn
            int maxDist = 13 + (int) (Math.sqrt(round) + Utility.getRandom(2));//maxZombie Distance
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (Utility.getRandom(600 + 40 * round) <= 2 && map[i][j] == 'o') {
                        map[i][j] = 'a';
                    }
                    if (Utility.getRandom(3500 + (55 * round)) <= 1 && map[i][j] == 'o') {
                        map[i][j] = 'g';
                    }
                    if ((Utility.getRandom(150) < difficulty)
                            && (Math.sqrt(Math.pow(p.getX() - i, 2) + Math.pow(p.getY() - j, 2))) > zDist && map[i][j] == 'o'
                            && j > 9 && pathFind[i][j] != 0) {
                        Zombie z = new Zombie(i, j);
                        zombies.add(z);
                        map[i][j] = 'z';
                        Thread t = new Thread(z);
                        t.start();
                    }
                }
            }
        }
        try {
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * checks to see if there are any zombies left on the map
     *
     * @return whether there are any live zombies left on the map or not
     */
    private boolean areZombies() {
        if (zombies != null) {
            for (Zombie z : zombies) {
                if (z != null) {
                    if (z.isAlive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
