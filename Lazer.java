package zombieMaze;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rohan
 */
public class Laser {

    public static int level = 1;
    public static int range = 5;//this is the range and can be upgraded in game
    public double x;
    public double y;

    /**
     * creates a laser and lets it do its thing
     *
     * @param inX
     * @param inY
     */
    public Laser(double inX, double inY) {
        x = inX;
        y = inY;
        fireMahLaser();//sorry had to
    }

    /**
     * Goes through and destroys everything in its range my apologies for the
     * name. It will not change @notTODO change the name
     */
    public void fireMahLaser() {
        for (double i = 1; i <= range; i += .01) {
            if(Map.p.breakingBlocks){
            try {
                if (Map.map[(int) (x + (i * Math.sin(Map.p.angle)))][(int) (y - (i * Math.cos(Map.p.angle)))] == 'w'
                        || Map.map[(int) (x + (i * Math.sin(Map.p.angle)))][(int) (y - (i * Math.cos(Map.p.angle)))] == 'z') {
                    Map.map[(int) (x + (i * Math.sin(Map.p.angle)))][(int) (y - (i * Math.cos(Map.p.angle)))] = 'o';
                }
            } catch (Exception e) {
                //System.out.println("HUH! Exception"+e);
            }}
            if (Map.zombies != null) {
                for (Zombie z : Map.zombies) {
                    if (z != null) {
                        if ((z.getY() == ((int) (y - (i * Math.cos(Map.p.angle)))))) {
                            if ((z.getX() == ((int) (x + (i * Math.sin(Map.p.angle)))))) {
                                        Map.score+=Utility.getRandom(5)+5;
                                z.die();
                            }
                        }
                    }
                }
            }
        }
    }
}
