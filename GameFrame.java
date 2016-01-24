/**
 * This code is liscenced under public domain as a recognition of thanks for the
 * various helpful community members who worked with us on this thing.
 */
package zombieMaze;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class GameFrame extends JFrame {

    /**
     * The amount of pixels in each tile here we are using 2 by 2 tiles
     */
    public static final int screenRes = 5;
    /**
     * the main JFRAME for the game
     */
    static GameFrame m;
    private static final long serialVersionUID = 1L;

    /**
     * Plays the game
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException {

        int[] temp = null;
        do {
            Map.score = 0;
            Map map = new Map();
            Thread t = new Thread(map);
            //System.out.println(Arrays.deepToString(Map.map));
            t.start();
            m = new GameFrame();
            while (Map.p.alive) {
                m.p.repaint();
            }
            UtilityIO.showPopUp("A zombie killed you.\nGame over!\nScore: "+Map.score);
            Map.round = 0;
            ArrayList<Integer> highScores = file.getArrayListIntFromFile("highScore.txt");
            if (Map.score > highScores.get(highScores.size() - 1)) {
                UtilityIO.showPopUp("YOU GOT A HIGH SCORE!: " + Map.score);
                highScores.add(Map.score);
                temp = new int[highScores.size()];
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = highScores.get(i);
                }
                Arrays.sort(temp);
                temp = Arrays.copyOfRange(temp, temp.length - 5, temp.length);
                file.writeIntArrayToFile("highScore.txt", temp);
            }
            temp = new int[highScores.size()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = highScores.get(i);
            }
            Arrays.sort(temp);
            temp = Arrays.copyOfRange(temp, temp.length - 5, temp.length);
            file.writeIntArrayToFile("highScore.txt", temp);
            Map.score = 0;
            UtilityIO.showPopUp("High Scores are:\n1." + temp[4] + "\n2." + temp[3] + "\n3." + temp[2] + "\n4." + temp[1] + "\n5." + temp[0]);
        } while (getBoolFromUser("Play Again?"));
        System.exit(0);
    }

    /**
     * gets boolean from the user
     *
     * @param prompt a prompt to display for the user
     * @return a boolean such that no response or any form of y Yes yes is true
     * anything else is false
     */
    public static boolean getBoolFromUser(String prompt) {
        UtilityIO.showPopUp("Play Again?");
        return true;
    }
    public JPanel p;

    /**
     * creates the game display frame
     */
    GameFrame() throws IOException {
        Map.score = 0;
        setSize(900, 900 + 22);
        setTitle("The Darkness Within");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.addKeyListener(new Controller());
        this.setResizable(false);
        p = new MyPanelA();
        this.add(p);
        this.pack();
        this.setBackground(new Color(5, 120, 156));
        this.addMouseListener(new MouseListener());
        this.addMouseMotionListener(new MouseListener());
    }

    static class MyPanel extends JPanel {

        public static BufferedImage gun;
        public static BufferedImage gunWithBoom;
        private static final long serialVersionUID = 1L;

        public MyPanel() throws IOException {
            gun = ImageIO.read(new File("gun.png"));
            gunWithBoom = ImageIO.read(new File("gunWithBoom.png"));
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.setBackground(new Color(5, 120, 156));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(880, 922);
        }

        /**
         * Updates the display
         *
         * @param g a graphic
         */
        public void twoD(Graphics g) {
            for (int i = 0; i < Map.map.length; i++) {
                for (int j = 0; j < Map.map[0].length; j++) {
                    if (((int) Map.p.x == i) && ((int) Map.p.y == j)) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                        g.setColor(new Color(5, 120, 156));
                        g.drawLine(i * screenRes + (screenRes / 2), j * screenRes + (screenRes / 2),
                          (int) (i * screenRes + (screenRes / 2) + screenRes * Math.sin(Map.p.angle)), (int) (j * screenRes + (screenRes / 2) - screenRes * Math.cos(Map.p.angle)));
                    } else if (Map.map[i][j] == 'g') {
                        g.setColor(Color.GREEN);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'a') {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'o') {
                        g.setColor(this.getBackground());
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'z') {
                        g.setColor(Color.RED);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'w') {
                        g.setColor(Color.black);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    }
                }
            }

            g.setColor(Color.blue);
            if (Map.p.isShooting) {

                g.drawImage(gunWithBoom, 154, 350, null);
                g.fillOval(445, 445, 10, 10);
                Map.p.isShooting = false;
            } else {
                g.drawImage(gun, 154, 350, null);
                g.drawOval(445, 445, 10, 10);
            }
        }

        public static final double fov = .3;

        /**
         * updates graphic
         *
         * @param board
         */
        @Override
        public void paintComponent(Graphics board) {
            super.paintComponent(board);

            double distance;
            double lineHeight = -1;
            double angleOff;
            double angleCast;
            for (int i = 0; i < 900; i++) {
                //i is the line across from the side.
                angleOff = Math.atan(((i - 450.0) / 4000.0) / fov);
                boolean hit = false;
                board.setColor(GameFrame.m.getBackground());
                board.drawLine(i, 922, i, 0);
                for (double rayDist = 0; !hit; rayDist += .01) {
                    try {
                        char block = Map.map[(int) (Map.p.x + Math.sin(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist)][(int) (Map.p.y - Math.cos(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist)];
                        if (block != 'o') {
                            lineHeight = 800 / (rayDist * Math.cos(angleOff));
                            switch (block) {
                                case 'w':
                                    board.setColor(Color.BLACK);
                                    break;
                                case 'z':
                                    board.setColor(Color.red);
                                    break;
                                case 'g':
                                    board.setColor(Color.green);
                                    break;
                                case 'a':
                                    board.setColor(Color.DARK_GRAY);
                                    break;
                            }
                            hit = true;
                        }
                    } catch (Exception e) {
                        hit = true;
                        lineHeight = 800 / (rayDist * Math.cos(angleOff));
                        board.setColor(Color.black);
                    }
                }

                board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
            }
            board.setColor(Color.CYAN);
            board.drawString("Grenades: " + Map.p.getGrenades() + " Lasers: " + Map.p.getLasers()
              + " Round: " + Map.round + " Gun Level: "
              + Laser.level + " Score: " + Map.score, 530, 15);
            board.setColor(Color.cyan);

            if (Map.zombies != null) {
                for (Zombie z : Map.zombies) {
                    if (z != null) {
                        if (Math.sqrt(Math.pow(Map.p.x - z.getX(), 2) + Math.pow(Map.p.y - z.getY(), 2)) < 6) {
                            board.drawString("WARNING ZOMBIE(S) nearby!", 15, 680);
                            break;
                        }
                    }
                }
            }
            board.setColor(Color.GRAY);
            board.drawString((int) ((Math.toDegrees(Map.p.angle) + 360) % 360) + "|" + Map.p.getX() + "|" + Map.p.getY(), 15, 650);
            twoD(board);
            if (Map.p.isGrenading) {
                Map.p.isGrenading = false;
                board.setColor(Color.red);
                board.fillRect(440, 440, 20, 20);
            }
        }
    }

    static class MyPanelA extends JPanel {

        public static BufferedImage gun;
        public static BufferedImage gunWithBoom;
        public static BufferedImage wallTexture;
        private static final long serialVersionUID = 1L;

        public MyPanelA() throws IOException {

            wallTexture = ImageIO.read(new File("stones1.jpg"));
            gun = ImageIO.read(new File("gun.png"));
            gunWithBoom = ImageIO.read(new File("gunWithBoom.png"));
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.setBackground(new Color(5, 120, 156));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(880, 922);
        }

        /**
         * Updates the display
         *
         * @param g a graphic
         */
        public void twoD(Graphics g) {
            for (int i = 0; i < Map.map.length; i++) {
                for (int j = 0; j < Map.map[0].length; j++) {
                    if (((int) Map.p.x == i) && ((int) Map.p.y == j)) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                        g.setColor(new Color(5, 120, 156));
                        g.drawLine(i * screenRes + (screenRes / 2), j * screenRes + (screenRes / 2),
                          (int) (i * screenRes + (screenRes / 2) + screenRes * Math.sin(Map.p.angle)), (int) (j * screenRes + (screenRes / 2) - screenRes * Math.cos(Map.p.angle)));
                    } else if (Map.map[i][j] == 'g') {
                        g.setColor(Color.GREEN);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'a') {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'o') {
                        g.setColor(this.getBackground());
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'z') {
                        g.setColor(Color.RED);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'w') {
                        g.setColor(Color.black);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    }
                }
            }

            g.setColor(Color.blue);
            if (Map.p.isShooting) {

                g.drawImage(gunWithBoom, 154, 350, null);
                g.fillOval(445, 445, 10, 10);
                Map.p.isShooting = false;
            } else {
                g.drawImage(gun, 154, 350, null);
                g.drawOval(445, 445, 10, 10);
            }
        }

        public static final double fov = .3;

        /**
         * updates graphic
         *
         * @param board
         */
        @Override
        public void paintComponent(Graphics board) {
            super.paintComponent(board);
            board.drawImage(wallTexture, 0, 0, null);

            double distance;
            double lineHeight = -1;
            double angleOff;
            double angleCast;
            double lastLineHeight = 900;
            for (int i = 0; i < 900; i++) {
                //i is the line across from the side.
                angleOff = Math.atan(((i - 450.0) / 4000.0) / fov);
                boolean hit = false;
                board.setColor(GameFrame.m.getBackground());
                // board.drawLine(i, 922, i, 0);
                for (double rayDist = 0; !hit; rayDist += .01) {
                    try {
                        char block = Map.map[(int) (Map.p.x + Math.sin(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist)][(int) (Map.p.y - Math.cos(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist)];
                        if (block != 'o') {

                            lineHeight = 800 / (rayDist * Math.cos(angleOff));
                            if (lineHeight < 0) {
                                System.err.println("How is the height negative");
                            }

                            switch (block) {
                                case 'w':
                                    if (461 - (lineHeight / 2) > 100 || rayDist < 8) {
                                        board.setColor(Color.BLACK);
                                        board.drawLine(i, (int) (461 + (lineHeight / 2)), i, (int) 900);
                                        board.drawLine(i, 0, i, (int) (461 - (lineHeight / 2)));
                                    } else {
                                        System.err.println("NASTY Artifact detected");
                                        board.drawLine(i, 0, i, 922);
                                    }
                                    break;

                                case 'z':
                                    board.setColor(Color.BLACK);
                                    board.drawLine(i, (int) 900, i, (int) (461 + (lineHeight / 2)));
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, 0);
                                    board.setColor(Color.red);
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
                                    break;

                                case 'g':
                                    board.setColor(Color.BLACK);
                                    board.drawLine(i, (int) 900, i, (int) (461 + (lineHeight / 2)));
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, 0);
                                    board.setColor(Color.green);
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
                                    break;
                                case 'a':
                                    board.setColor(Color.BLACK);
                                    board.drawLine(i, (int) 900, i, (int) (461 + (lineHeight / 2)));
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, 0);
                                    board.setColor(Color.DARK_GRAY); 
                                    board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
                                    break;

                            }
                            hit = true;
                            if (Math.abs(lastLineHeight - lineHeight) * rayDist > 57) {
                                board.setColor(Color.black);
                                board.fillRect(i - 4, 0, 5, 922);

                            }
                            lastLineHeight = lineHeight;
                        }
                    } catch (Exception e) {
                        hit = true;
                        lineHeight = 800 / (rayDist * Math.cos(angleOff));
                        board.setColor(Color.black);
                        board.drawLine(i, (int) (461 + (lineHeight / 2)), i, (int) 900);
                        board.drawLine(i, 0, i, (int) (461 - (lineHeight / 2)));

                    }
                }

            }
            board.setColor(Color.CYAN);
            board.drawString("Grenades: " + Map.p.getGrenades() + " Lasers: " + Map.p.getLasers()
              + " Round: " + Map.round + " Gun Level: "
              + Laser.level + " Score: " + Map.score, 530, 15);
            board.setColor(Color.cyan);

            if (Map.zombies != null) {
                for (Zombie z : Map.zombies) {
                    if (z != null) {
                        if (Math.sqrt(Math.pow(Map.p.x - z.getX(), 2) + Math.pow(Map.p.y - z.getY(), 2)) < 6) {
                            board.drawString("WARNING ZOMBIE(S) nearby!", 15, 680);
                            break;
                        }
                    }
                }
            }
            board.setColor(Color.GRAY);
            board.drawString((int) ((Math.toDegrees(Map.p.angle) + 360) % 360) + "|" + Map.p.getX() + "|" + Map.p.getY(), 15, 650);
            twoD(board);
            if (Map.p.isGrenading) {
                Map.p.isGrenading = false;
                board.setColor(Color.red);
                board.fillRect(440, 440, 20, 20);
            }
        }
    }

    public static class MyPanelT extends JPanel {

        private static final long serialVersionUID = 1L;
        public static BufferedImage WALL;
        public static BufferedImage PLAYER;
        public static BufferedImage RAT;
        public static BufferedImage HEALTH;
        public static Texture WALLT;
        public static Texture PLAYERT;
        public static Texture RATT;
        public static Texture HEALTHT;

        public MyPanelT() {
            try {
                WALL = ImageIO.read(new File("wall.jpg"));
                PLAYER = ImageIO.read(new File("player.jpg"));
                RAT = ImageIO.read(new File("rat.jpg"));
                HEALTH = ImageIO.read(new File("health.jpg"));
                WALLT = new Texture(WALL);
                PLAYERT = new Texture(PLAYER);
                RATT = new Texture(RAT);
                HEALTHT = new Texture(HEALTH);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e);
                System.exit(1);
            }
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.setBackground(new Color(5, 120, 156));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(880, 922);
        }

        /**
         * Updates the display
         *
         * @param g a graphic
         */
        public void twoD(Graphics g) {
            for (int i = 0; i < Map.map.length; i++) {
                for (int j = 0; j < Map.map[0].length; j++) {
                    if (((int) Map.p.x == i) && ((int) Map.p.y == j)) {
                        g.setColor(Color.orange);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                        g.setColor(new Color(5, 120, 156));
                        g.drawLine(i * screenRes + (screenRes / 2), j * screenRes + (screenRes / 2),
                          (int) (i * screenRes + (screenRes / 2) + screenRes * Math.sin(Map.p.angle)), (int) (j * screenRes + (screenRes / 2) - screenRes * Math.cos(Map.p.angle)));
                    } else if (Map.map[i][j] == 'g') {
                        g.setColor(Color.GREEN);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'a') {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'o') {
                        g.setColor(this.getBackground());
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'z') {
                        g.setColor(Color.RED);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'w') {
                        g.setColor(Color.black);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    }
                }
            }
            g.setColor(Color.blue);
            g.drawOval(445, 445, 10, 10);
        }

        public static final double fov = .3;

        /**
         * updates graphic
         *
         * @param board
         */
        @Override
        public void paintComponent(Graphics board) {
            super.paintComponent(board);
            board.setColor(new Color(4, 99, 128));
            board.drawRect(0, 0, 462, 462);
            double distance;
            double lineHeight = -1;
            double angleOff;
            double angleCast;
            double arbLowNum = .025;
            for (int i = 0; i < 900; i++) {
                //i is the line across from the side.
                angleOff = Math.atan(((i - 450.0) / 4000.0) / fov);
                boolean hit = false;
                //board.setColor(GameFrame.m.getBackground());
                //board.drawLine(i, 922, i, 0);
                for (double rayDist = 0; !hit; rayDist += .01) {
                    try {
                        double xHit = (Map.p.x + Math.sin(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist);
                        double yHit = (Map.p.y - Math.cos(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist);
                        char block = Map.map[(int) xHit][(int) yHit];
                        int lineAcrossHit;
                        if (Math.abs((yHit - 1) % 1) < arbLowNum) {
                            lineAcrossHit = (int) (64 * Math.abs(xHit % 1));
                        } else if (Math.abs((xHit - 1) % 1) < arbLowNum) {
                            lineAcrossHit = (int) (64 * Math.abs(yHit % 1));
                        } else {
                            //System.err.println("should this happen?");
                            lineAcrossHit = 0;
                        }
                        System.out.println("A hit at " + lineAcrossHit);
                        if (block != 'o') {
                            lineHeight = 800 / (rayDist * Math.cos(angleOff));
                            switch (block) {
                                case 'w':

                                    int startingHeight = (int) (400 - lineHeight / 2);
                                    int endingHeight = (int) (400 - lineHeight / 2);
                                    for (int a = startingHeight; a < endingHeight; a++) {
                                        int numOfPixels = a - startingHeight;
                                        board.setColor(WALLT.texture[(int) (numOfPixels / lineHeight) * 64][lineAcrossHit]);
                                        board.drawRect(i, a, 0, 0);
                                    }
                                    break;
                                case 'z':
                                    startingHeight = (int) (400 - lineHeight / 2);
                                    endingHeight = (int) (400 - lineHeight / 2);
                                    for (int a = startingHeight; a < endingHeight; a++) {
                                        int numOfPixels = a - startingHeight;
                                        board.setColor(RATT.texture[(int) (numOfPixels / lineHeight) * 64][lineAcrossHit]);
                                        board.drawRect(i, a, 0, 0);
                                    }
                                    break;
                                case 'g':
                                    startingHeight = (int) (400 - lineHeight / 2);
                                    endingHeight = (int) (400 - lineHeight / 2);
                                    for (int a = startingHeight; a < endingHeight; a++) {
                                        int numOfPixels = a - startingHeight;
                                        board.setColor(HEALTHT.texture[(int) (numOfPixels / lineHeight) * 64][lineAcrossHit]);
                                        board.drawRect(i, a, 0, 0);
                                    }
                                    break;
                                case 'a':
                                    startingHeight = (int) (400 - lineHeight / 2);
                                    endingHeight = (int) (400 - lineHeight / 2);
                                    for (int a = startingHeight; a < endingHeight; a++) {
                                        int numOfPixels = a - startingHeight;
                                        board.setColor(HEALTHT.texture[(int) (numOfPixels / lineHeight) * 64][lineAcrossHit]);
                                        board.drawRect(i, a, 0, 0);
                                    }
                                    break;
                            }
                            hit = true;
                        }
                    } catch (Exception e) {
                        hit = true;
                        lineHeight = 800 / (rayDist * Math.cos(angleOff));
                        board.setColor(Color.black);
                        board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
                    }
                }

                // board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
            }
            board.setColor(Color.CYAN);
            board.drawString("Grenades: " + Map.p.getGrenades() + " Lasers: " + Map.p.getLasers()
              + " Round: " + Map.round + " Gun Level: "
              + Laser.level, 590, 15);
            board.setColor(Color.cyan);

            if (Map.zombies != null) {
                for (Zombie z : Map.zombies) {
                    if (z != null) {
                        if (Math.sqrt(Math.pow(Map.p.x - z.getX(), 2) + Math.pow(Map.p.y - z.getY(), 2)) < 6) {
                            board.drawString("WARNING ZOMBIE(S) nearby!", 15, 850);
                            break;
                        }
                    }
                }
            }
            board.setColor(Color.GRAY);
            board.drawString((int) ((Math.toDegrees(Map.p.angle) + 360) % 360) + "|" + Map.p.getX() + "|" + Map.p.getY(), 15, 900);
            twoD(board);
        }
    }

    static class MyPanelG extends JPanel {

        private static final long serialVersionUID = 1L;
        public static BufferedImage WALL;
        public static BufferedImage PLAYER;
        public static BufferedImage RAT;
        public static BufferedImage HEALTH;
        public static Texture WALLT;
        public static Texture PLAYERT;
        public static Texture RATT;
        public static Texture HEALTHT;

        public MyPanelG() {
            try {
                WALL = ImageIO.read(new File("wall.jpg"));
                PLAYER = ImageIO.read(new File("player.jpg"));
                RAT = ImageIO.read(new File("wall.jpg"));
                HEALTH = ImageIO.read(new File("health.jpg"));

                WALLT = new Texture(WALL);
                PLAYERT = new Texture(PLAYER);
                RATT = new Texture(RAT);
                HEALTHT = new Texture(HEALTH);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e);
                System.exit(1);
            }
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.setBackground(new Color(5, 120, 156));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(880, 922);
        }

        /**
         * Updates the display
         *
         * @param g a graphic
         */
        public void twoD(Graphics g) {
            for (int i = 0; i < Map.map.length; i++) {
                for (int j = 0; j < Map.map[0].length; j++) {
                    if (((int) Map.p.x == i) && ((int) Map.p.y == j)) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                        g.setColor(new Color(5, 120, 156));
                        g.drawLine(i * screenRes + (screenRes / 2), j * screenRes + (screenRes / 2),
                          (int) (i * screenRes + (screenRes / 2) + screenRes * Math.sin(Map.p.angle)), (int) (j * screenRes + (screenRes / 2) - screenRes * Math.cos(Map.p.angle)));
                    } else if (Map.map[i][j] == 'g') {
                        g.setColor(Color.GREEN);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'a') {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'o') {
                        g.setColor(this.getBackground());
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'z') {
                        g.setColor(Color.RED);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    } else if (Map.map[i][j] == 'w') {
                        g.setColor(Color.black);
                        g.fillRect(i * screenRes, screenRes * j, screenRes, screenRes);
                    }
                }
            }
            g.setColor(Color.blue);
            g.drawOval(445, 445, 10, 10);
        }

        public static final double fov = .7;

        /**
         * updates graphic
         *
         * @param board
         */
        @Override
        public void paintComponent(Graphics board) {
            super.paintComponent(board);
            double distance;
            double lineHeight = -1;
            double angleOff;
            double angleCast;
            for (int i = 0; i < 900; i++) {
                //i is the line across from the side.
                angleOff = Math.atan(((i - 450.0) / 4000.0) / fov);
                boolean hit = false;
                board.setColor(GameFrame.m.getBackground());
                board.drawLine(i, 922, i, 0);
                for (double rayDist = 0; !hit; rayDist += .01) {
                    try {
                        double xHit = (Map.p.x + Math.sin(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist);
                        double yHit = (Map.p.y - Math.cos(Player.normaliseAngle(angleOff + Map.p.angle)) * rayDist);
                        char block = Map.map[(int) xHit][(int) yHit];
                        if (block != 'o') {
                            int textureLine = getPixelLineOnTexture(xHit, yHit, angleOff + Map.p.angle, rayDist);
                            lineHeight = 800.0 / (rayDist * Math.cos(angleOff));
                            switch (block) {
                                case 'w':
                                    /*
                                    boolean isDarkerColor = false;
                                    double stripeHeight = (lineHeight / 10.0);
                                    for (int a = (int) (461 - (lineHeight / 2.0)); a <= 460 + (lineHeight / 2.0) - stripeHeight; a += stripeHeight) {
                                        if (isDarkerColor) {
                                            board.setColor(Color.DARK_GRAY);
                                        } else {
                                            board.setColor(Color.LIGHT_GRAY);
                                        }
                                        board.drawRect(i, a, 0, (int) stripeHeight);
                                        isDarkerColor = !isDarkerColor;
                                    }
                                     */
                                    int p = 0;//too lazy

                                    int stepHeight = (int) (lineHeight / Texture.TILE_SIZE);
                                    for (int a = (int) (450 - lineHeight / 2); a < 450 + lineHeight / 2; a++) {
                                        board.setColor(WALLT.texture[textureLine][(int) (p / lineHeight * Texture.TILE_SIZE)]);
                                        board.drawRect(i, a, 0, stepHeight);
                                        p++;
                                    }
                                    break;
                                case 'z':
                                    board.setColor(Color.red);

                                    board.drawLine(i, (int) (450 - (lineHeight / 2)), i, (int) (450 + (lineHeight / 2)));
                                    break;
                                case 'g':
                                    board.setColor(Color.green);

                                    board.drawLine(i, (int) (450 - (lineHeight / 2)), i, (int) (450 + (lineHeight / 2)));
                                    break;
                                case 'a':
                                    board.setColor(Color.DARK_GRAY);

                                    board.drawLine(i, (int) (450 - (lineHeight / 2)), i, (int) (450 + (lineHeight / 2)));
                                    break;
                            }
                            hit = true;
                        }
                    } catch (Exception e) {
                        hit = true;
                        lineHeight = 800 / (rayDist * Math.cos(angleOff));
                        board.setColor(Color.black);

                        board.drawLine(i, (int) (461 - (lineHeight / 2)), i, (int) (461 + (lineHeight / 2)));
                    }
                }

            }
            board.setColor(Color.CYAN);
            board.drawString("Grenades: " + Map.p.getGrenades() + " Lasers: " + Map.p.getLasers()
              + " Round: " + Map.round + " Gun Level: "
              + Laser.level + " Score: " + Map.score, 530, 15);
            board.setColor(Color.cyan);

            if (Map.zombies != null) {
                for (Zombie z : Map.zombies) {
                    if (z != null) {
                        if (Math.sqrt(Math.pow(Map.p.x - z.getX(), 2) + Math.pow(Map.p.y - z.getY(), 2)) < 6) {
                            board.drawString("WARNING ZOMBIE(S) nearby!", 15, 850);
                            break;
                        }
                    }
                }
            }
            board.setColor(Color.GRAY);
            board.drawString((int) ((Math.toDegrees(Map.p.angle) + 360) % 360) + "|" + Map.p.getX() + "|" + Map.p.getY(), 15, 900);
            twoD(board);
        }
        public final int size = MazeHandler.size;

        private int getPixelLineOnTexture(double xHit, double yHit, double angle, double rayDist) {
            if ((xHit < 0 || xHit >= size) && ((yHit < 0 || yHit > size))) {
                return 0;
            }
            if (yHit < 0) {
                return (int) ((xHit % 1.0) * (Texture.TILE_SIZE / 1.0));
            }
            if (yHit >= size) {
                return (int) ((1 - (xHit % 1.0)) * (Texture.TILE_SIZE / 1.0));
            }
            if (xHit < 0) {
                return (int) ((1 - (yHit % 1.0)) * (Texture.TILE_SIZE / 1.0));
            }
            if (xHit >= size) {
                return (int) (((yHit % 1.0)) * (Texture.TILE_SIZE / 1.0));
            }

            int xPulledOut = (int) (Map.p.x + (Math.sin(Player.normaliseAngle(angle)) * (rayDist - .5)));
            int yPulledOut = (int) (Map.p.y - (Math.cos(Player.normaliseAngle(angle)) * (rayDist - .5)));
            int intXHit = (int) xHit;
            int intYHit = (int) yHit;
            if (xPulledOut != intXHit && yPulledOut != intYHit) {
                return 0;
            }
            if (xPulledOut > intXHit) {
                return (int) ((1 - (yHit % 1)) * Texture.TILE_SIZE);
            }
            if (xPulledOut < intXHit) {
                return (int) (((yHit % 1)) * Texture.TILE_SIZE);
            }
            if (yPulledOut > intYHit) {
                return (int) ((1 - (xHit % 1)) * Texture.TILE_SIZE);
            }
            if (yPulledOut < intYHit) {
                return (int) (((xHit % 1)) * Texture.TILE_SIZE);
            }

            return -1;
        }
    }
}
