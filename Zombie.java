package zombieMaze;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Intelligent zombie which can move around the maze
 *
 * @author rohan
 */
public class Zombie implements Runnable {

    private int x;
    private int y;
    private boolean alive = true;

    /**
     * creates a zombie at the given coordinates
     *
     * @param inX the x coordinate to create with
     * @param inY the y coordinate to create with
     */
    public Zombie(int inX, int inY) {
        x = inX;
        y = inY;
    }

    /**
     * Kills off the zombie and fixes map accordingly
     */
    public void die() {

        alive = false;
        Map.map[x][y] = 'o';
        try {
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(Zombie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This code lets the zombie try to find the player and kill him
     */
    @Override
    public void run() {
        life:
        while (isAlive() && Map.p.alive) {
            try {
                Thread.sleep(1500 - 30 * Map.round);
            } catch (Exception e) {
            }
            int oldX = x;
            int oldY = y;
            int[] movementCoords = new int[2];
            int bestSquareValue = 99999;
            try {
                if (Map.map[oldX + 1][oldY] != 'w') {
                    int potential = Map.pathFind[oldX + 1][oldY];
                    if (potential < bestSquareValue) {
                        bestSquareValue = potential;
                        movementCoords[0] = oldX + 1;
                        movementCoords[1] = oldY;
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (Map.map[oldX - 1][oldY] != 'w') {
                    int potential = Map.pathFind[oldX - 1][oldY];
                    if (potential < bestSquareValue) {
                        bestSquareValue = potential;
                        movementCoords[0] = oldX - 1;
                        movementCoords[1] = oldY;
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (Map.map[oldX][oldY + 1] != 'w') {
                    int potential = Map.pathFind[oldX][oldY + 1];
                    if (potential < bestSquareValue) {
                        bestSquareValue = potential;
                        movementCoords[0] = oldX;
                        movementCoords[1] = oldY + 1;
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (Map.map[oldX][oldY - 1] != 'w') {
                    int potential = Map.pathFind[oldX][oldY - 1];
                    if (potential < bestSquareValue) {
                        bestSquareValue = potential;
                        movementCoords[0] = oldX;
                        movementCoords[1] = oldY - 1;
                    }
                }
            } catch (Exception e) {
            }
            if (isAlive()) {
                Map.map[oldX][oldY] = 'o';
                Map.map[movementCoords[0]][movementCoords[1]] = 'z';
                x = movementCoords[0];
                y = movementCoords[1];
            }

            if (Math.abs(this.x - Map.p.getX()) + Math.abs(this.y - Map.p.getY()) <= 1.42 && isAlive()) {
                try {
                    Thread.sleep(1000);//how long it takes a zombie to reach its hand out and kill you
                } catch (InterruptedException ex) {
                    Logger.getLogger(Zombie.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (Math.abs(this.x - Map.p.x) + Math.abs(this.y - Map.p.y) < 1.42 && isAlive()) {
                    Map.p.alive = false;//Game Over!
                    break life;
                }
            }
        }
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return whether this is alive or not
     */
    public boolean isAlive() {
        return alive;
    }

}
