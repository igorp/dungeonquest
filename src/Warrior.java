//Warrior by Igor P. (c)
//This class is on the top one, it's the frame on which all is drawn.
//Note that offsets are only added at drawing process, otherwise dont worry about them

import java.awt.*;
import java.awt.image.*;
import java.awt.image.BufferStrategy.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class Warrior extends javax.swing.JFrame implements ComponentListener {

    public static enum DeathType {

        PIT, FIRE, ENEMY
    }

    public static enum Direction {

        UP, DOWN, LEFT, RIGHT
    }
    //40 is pretty good
    static final long UPDATE_PERIOD = 40;
    static final int offsetX = 3; //9
    static final int offsetY = 23; //30
    boolean looping = true;
    Logic logic = new Logic();
    long FPS = 0;
    int FPScounter = 0;
    long FPStick = System.currentTimeMillis();
    int animationIterator = 2;
    int attackIterator = 0;
    int timer = 0;
    static boolean developerView = false;
    long tick = System.currentTimeMillis();
    long periodTime = System.currentTimeMillis();
    long timeToSleep = 0;
    GameKey gameKey;
    AePlayWave sound;
    AePlayWave soundEnd;
    //use later for quitting game
    //public boolean stopped = false;
    Point2[] tilesToUpdate = new Point2[100];
    Map warriorMap;
    Hero hero;

    public Warrior() {
        //Frame related tasks
        super("Dungeon Quest");
        setIconImage(new ImageIcon("images/hero/hero_front2.png").getImage());
        initializeObjects();

        setFrame();

        gameKey = new GameKey();
        sound = new AePlayWave("music/demo.wav");
        addKeyListener(gameKey);

        sound.start();
        createBufferStrategy(2);
        gameLoop();
    }

    public void setFrame() {
        //the int after offset is for linux
        Dimension d = new Dimension(warriorMap.width * 40 + offsetX, warriorMap.height * 40 + offsetY);
        setSize(d);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void initializeObjects() {
        warriorMap = logic.getMap();
        hero = logic.getHero();
    }

    public void drawScreen(boolean drawAll) {
        BufferStrategy buffer = this.getBufferStrategy();
        Graphics g = null;
        g = buffer.getDrawGraphics();

        drawObjects(g, drawAll);
        drawLives(g);

        g.dispose();
        buffer.show();
        Toolkit.getDefaultToolkit().sync();
    }

    public void drawObjects(Graphics g, boolean drawAll) {

        if (drawAll) {
            warriorMap.drawMap(g);
            logic.drawCharacters(g);
        } else {
            warriorMap.drawMap(g, developerView, tilesToUpdate);
            logic.drawCharacters(g);
        }
    }

    public void drawLives(Graphics g) {
        BufferedImage image = null;
        BufferedImage image2 = null;
        try {
            image = ImageIO.read(new File("images/heart.png"));
        } catch (IOException e) {
        }
        for (int j = 0; j < logic.getLives(); j++) {
            g.drawImage(image, 15 + 26 * j, 30, null);
        }
        try {
            image2 = ImageIO.read(new File("images/goldCount.png"));
        } catch (IOException e) {
        }
        g.drawImage(image2, 15, 60, null);
        g.fillRect(40, 60, 20, 20);
        g.setColor(Color.YELLOW);
        String x = logic.getGoldCount() + " ";
        Font font = new Font("Dialog", Font.BOLD, 20);
        g.setFont(font);
        g.drawString(x, 45, 80);
        g.setColor(Color.BLACK);
    }

    public void drawFPS(Graphics g) {
        if (developerView) {
            //g.drawString("FPS: " + FPS, offsetX+1, offsetY+11);
        }
    }

    public void gameLoop() {
        //make sure you draw
        boolean once = true;

        while (true) {
            //we have to know the tiles before we update the game (causing the wrong tiles to repaint)
            //dynamic images are also set
            tilesToUpdate = logic.getRepaintTiles();
            updateGame();
            countFPS();
            drawScreen(logic.repaintWholeMap());

            periodTime = System.currentTimeMillis() - tick;
            timeToSleep = UPDATE_PERIOD - periodTime;

            if (timeToSleep > 0) {
                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }
            }
            tick = System.currentTimeMillis();
            if (once) {
                drawScreen(true);
                once = false;
            }
        }
    }

    public void updateGame() {
        logic.updateTimer(timer);
        //Hero
        if (hero.getAlive()) {
            if (!checkGameInput()) {
                hero.setStaticImage();
            }
            logic.updateEnemy();
            logic.checkDeadlyTilesCurrent();

            handleAttack();
            logic.updateAttack();

            //in case hero dies
            if (!hero.getAlive()) {
                //subtract one life and repaint the hearts in left top corner
                soundEnd = new AePlayWave("music/end.wav");
                //soundEnd.start();
                logic.editLives(-1);
                drawScreen(true);
            }
        } else {
            //loop on the death animation
            displayDeathAnimation(animationIterator++);
            if (animationIterator > 50) {
                if (logic.getLives() == 0) {
                    //quit game
                    dispose();
                    System.exit(0);
                }

                //restart the game
                logic.respawn();
                logic.resetIterator();
                animationIterator = 2;
            }
        }
    }

    public void handleAttack() {
        if (hero.isAttacking()) {
            hero.setAttackingImg();
            //System.out.println("iter " + attackIterator);
            attackIterator++;
            if (hero.endAttack(attackIterator)) {
                attackIterator = 0;
                waitAttackIter = 0;
            }
        } else {
            waitAttackIter++;
        }
    }

    public void displayDeathAnimation(int iterator) {
        switch (hero.getDeath()) {
            case PIT:
                logic.displayPitAnimation(iterator);
                break;
            case FIRE:
                logic.displayFireAnimation(iterator);
                break;
            case ENEMY:
                logic.displayKillAnimation(iterator);
                break;
            default:
                break;
        }
    }

    public void countFPS() {
        FPScounter++;
        if (System.currentTimeMillis() - FPStick > 1000) {
            FPStick = System.currentTimeMillis();
            FPS = FPScounter;
            FPScounter = 0;
        }
    }

    public static void main(String[] args) {
        Warrior w = null;
        w = new Warrior();
    }

    public boolean checkGameInput() {
        boolean moving = false;

        if (gameKey.key_down) {
            logic.tryMoving(Direction.DOWN);
            moving = true;
        } else if (gameKey.key_left) {
            logic.tryMoving(Direction.LEFT);
            moving = true;
        } else if (gameKey.key_up) {
            logic.tryMoving(Direction.UP);
            moving = true;
        } else if (gameKey.key_right) {
            logic.tryMoving(Direction.RIGHT);
            moving = true;
        }
        if (gameKey.key_space) {
            if (!hero.isAttacking() && waitAttackIter > 10) {
                hero.setAttacking(true);
            }
        }
        if (gameKey.key_x) {
            hero.setImage("images/hero/hero_front_defend.png");
        }
        return moving;
    }
    int waitAttackIter = 0;

    public void componentResized(ComponentEvent e) {
        drawScreen(true);
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
}
