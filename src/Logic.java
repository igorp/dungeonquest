//Warrior by Igor P. (c)
//Interaction between player, map and enemies. collision detection etc.
//Serves as an intermediate between Warrior and lower classes

import java.awt.*;
import java.util.Random;

public class Logic {

    //each level consists of maps
    int mapIndexX = 0;
    int mapIndexY = 0;
    Map map;
    private boolean repaintWholeMap = false;
    Hero hero;
    Character[] enemy = new Character[20];
    int enemyCount;
    Prop[] props = new Prop[20];
    int propCount;
    Point2 previousLocation;
    Warrior.Direction previousDirection;
    Random rand;
    Collisions collision;
    int timer;
    final static int X = 2;
    final static int Y_UP = 25;
    static final int DEADLY = 15;
    static final int Y_DEADLY = 39;
    public static Point2 deathTilePoint;
    public static Point2 deathLocation;
    private static Tile deathTile;
    private int lives = 3;
    private int gold = 0;

    public Logic() {
        map = new Map("maps/0x0.map");
        initializeLogic();
    }

    public void initializeLogic() {

        hero = new Hero("images/hero/hero_front2.png");
        hero.setLocation(map.getHeroStartLocation());

        enemy = map.getEnemies();
        props = map.getProps();

        enemyCount = map.getEnemyCount();
        propCount = map.getPropCount();

        deathTile = new Tile();
        deathTile.setImage("");

        previousLocation = hero.getLocation();

        rand = new Random();
        collision = new Collisions(map, propCount, props, enemy);
    }

    public void respawn() {
        hero.setAlive(true);
        reloadMap(0, 0);
        hero.setLocation(map.getHeroStartLocation());
    }

    public void drawCharacters(Graphics g) {
        //draw props
        for (int x = 0; x < propCount; x++) {
            //-10 is for looks, also the item must be visible
            hero.getLocation().getY(); //TEMP
            if (props[x].getLocation().getY() - 10 < hero.getLocation().getY() && props[x].isVisible()) {
                props[x].drawProp(g);
            }
        }
        //hero is in front of enemy
        for (int x = 0; x < enemyCount; x++) {
            //the end is if the enemy is dead
            if (enemy[x].getLocation().getY() < hero.getLocation().getY() || !enemy[x].getAlive()) {
                enemy[x].drawCharacter(g);
            }
        }
        hero.drawCharacter(g);

        for (int x = 0; x < propCount; x++) {
            if (props[x].getLocation().getY() - 10 >= hero.getLocation().getY() && props[x].isVisible()) {
                props[x].drawProp(g);
            }
        }

        //hero is behind enemy
        for (int x = 0; x < enemyCount; x++) {
            //the end is if the enemy is dead
            if (enemy[x].getLocation().getY() >= hero.getLocation().getY() && enemy[x].getAlive()) {
                enemy[x].drawCharacter(g);
            }
        }
        hero.drawBlow(g);
    }

    public Map getMap() {
        return map;
    }

    public Hero getHero() {
        return hero;
    }

    public void updateEnemy() {
        //Skeleton AI
        for (int x = 0; x < enemyCount; x++) {
            if (enemy[x] instanceof Skeleton) {
                updateSkeleton(x);
            }
            if (enemy[x] instanceof Rat) {
                updateRat(x);
            }
        }
    }

    private void updateSkeleton(int x) {
        enemy[x].getNextPoint(enemy[x].getDirection());
        if (!collision.withTilesArePresent(enemy[x])) {
            enemy[x].setLocation(enemy[x].getNextLocation());
            enemy[x].updateImage();
        } else {
            if (enemy[x].getDirection() == Warrior.Direction.DOWN) {
                enemy[x].setDirection(Warrior.Direction.UP);
            } else {
                enemy[x].setDirection(Warrior.Direction.DOWN);
            }
        }
    }

    private void updateRat(int x) {
        if (enemy[x].getAlive()) {
            enemy[x].getNextPoint(enemy[x].getDirection());
            if (collision.ratCollides(x)) {
                enemy[x].setDirection(setRatDirection(x));
                shiftNearBorders(x);
                enemy[x].updateImage();
            } else {
                if (collision.withEnemies(hero, enemy[x]) == null) {
                    enemy[x].setLocation(enemy[x].getNextLocation());
                    enemy[x].addDistanceTravelled();
                    enemy[x].updateImage();
                    ratAttack(x, false);
                } else {
                    ratAttack(x, true);
                }
            }
        } else {
            enemy[x].displayDeathAnimation();
        }
    }

    //first we wait for 5 cycles, then attack
    private void ratAttack(int x, boolean collision) {
        Rat rat = new Rat("images/rat/rat_left_attack.png");
        rat = (Rat) enemy[x];

        if (collision) {
            rat.addAttackTime();
            if (rat.getAttackTime() > 20) {
                rat.setBitingImage();
                if (rat.getAttackTime() == 25) {
                    rat.resetAttackTimer();
                }
            } else {
                rat.setStaticImage();
            }
        } else {
            rat.resetAttackTimer();
        }
    }

    private void shiftNearBorders(int x) {
        if (enemy[x].getLocation().getX() < 0) {
            enemy[x].setLocation(new Point2(enemy[x].getLocation().getX() + 1, enemy[x].getLocation().getY()));
        }
        if (enemy[x].getLocation().getX() > (map.width - 1) * 40) {
            enemy[x].setLocation(new Point2(enemy[x].getLocation().getX() - 1, enemy[x].getLocation().getY()));
        }
        if (enemy[x].getLocation().getY() < 0) {
            enemy[x].setLocation(new Point2(enemy[x].getLocation().getX(), enemy[x].getLocation().getY() + 1));
        }
        if (enemy[x].getLocation().getY() > map.width * 40) {
            enemy[x].setLocation(new Point2(enemy[x].getLocation().getX(), enemy[x].getLocation().getY() - 1));
        }
    }

    private Warrior.Direction setRatDirection(int x) {
        Warrior.Direction dir = null;
        //Find Hero by picking the direction he's in
        if (enemy[x].getDirection() == Warrior.Direction.RIGHT
                || enemy[x].getDirection() == Warrior.Direction.LEFT) {
            if (enemy[x].getLocation().getY() < hero.getLocation().getY()) {
                dir = Warrior.Direction.DOWN;
            } else {
                dir = Warrior.Direction.UP;
            }
        } else {
            if (enemy[x].getLocation().getX() < hero.getLocation().getX()) {
                dir = Warrior.Direction.RIGHT;
            } else {
                dir = Warrior.Direction.LEFT;
            }
        }

        if (enemy[x].getDistanceTravelled() == 0) {
            if (dir == Warrior.Direction.RIGHT) {
                dir = Warrior.Direction.LEFT;
            } else if (dir == Warrior.Direction.LEFT) {
                dir = Warrior.Direction.RIGHT;
            } else if (dir == Warrior.Direction.UP) {
                dir = Warrior.Direction.DOWN;
            } else {
                dir = Warrior.Direction.UP;
            }
        }

        enemy[x].resetDistanceTravelled();
        return dir;
    }

    public void tryMoving(Warrior.Direction d) {
        //for getRepaintTiles to select them correctly
        previousLocation = hero.getLocation();
        previousDirection = d;

        hero.getNextPoint(d);
        if (!hero.isAttacking()) {
            hero.setBlowLocation();
        }

        //next move
        if (collision.withDeadlyTiles(hero) != null) {
            deathTilePoint = collision.withDeadlyTiles(hero);
            hero.setAlive(false);
        }

        //gives the type of collision that took place with prop
        int propCollision = collision.withPropsArePresent(hero);
        //collision with walls
        if (!collision.withTilesArePresent(hero) && propCollision == 0) {
            hero.setLocation(hero.getNextLocation());
            hero.updateImage();
        }

        if (propCollision == 1) {
            editLives(1);
        }
        if (propCollision == 2) {
            gold++;
        }

        //if outside map, change map
        checkOutOfScreen();
    }

    private void checkOutOfScreen() {
        if (hero.getLocation().getX() > map.width * 40 || hero.getLocation().getX() < -40
                || hero.getLocation().getY() > map.height * 40 || hero.getLocation().getY() < -40) {
            setLocationAfterScreenChange();
        }
    }

    private void reloadMap(int x, int y) {
        map.reloadMap(x, y);
        repaintWholeMap = true;

        enemy = map.getEnemies();
        props = map.getProps();
        enemyCount = map.getEnemyCount();
        propCount = map.getPropCount();
        deathTile = new Tile();
        deathTile.setImage("");
    }

    private void setLocationAfterScreenChange() {
        if (hero.getLocation().getX() > map.width * 40) {
            hero.setLocation(-40, hero.getLocation().getY());
            mapIndexX++;
        }
        if (hero.getLocation().getX() < -40) {
            hero.setLocation(map.width * 40, hero.getLocation().getY());
            mapIndexX--;
        }
        if (hero.getLocation().getY() > map.height * 40 - 40) {
            hero.setLocation(hero.getLocation().getX(), -40);
        }
        if (hero.getLocation().getY() < -40) {
            hero.setLocation(hero.getLocation().getX(), map.height * 40);
        }
    }

    public boolean repaintWholeMap() {
        if (repaintWholeMap) {
            p("repaint");
            repaintWholeMap = false;
            return true;
        } else {
            return false;
        }
    }

    public void checkDeadlyTilesCurrent() {
        //collision with deadly tiles
        if (collision.withDeadlyTilesCurrent(hero, deathTilePoint) != null) {
            deathTilePoint = collision.withDeadlyTilesCurrent(hero, deathTilePoint);
            hero.setAlive(false);
        }
        //collision with monsters
        for (int x = 0; x < enemyCount; x++) {
            if (enemy[x].getAlive() && collision.withEnemies(hero, enemy[x]) != null) {
                //deathLocation = collision.withEnemies(hero, enemy[x]);
                hero.setAlive(false);
            }
        }
    }

    public Point2[] getRepaintTiles() {
        Point2[] repaintTile = new Point2[100];
        int counter = 0;

        for (int i = 0; i < Map.width; i++) {
            for (int j = 0; j < Map.height; j++) {
                //repaint tiles that are under hero
                if (collision.twoEntitiesCollideCurrent(hero, map.getTile(i, j), 0, 0, 0)) {
                    repaintTile[counter++] = new Point2(i, j);
                }
                //repaint deadly ones if hero is dead
                if (!hero.getAlive() && map.getTile(i, j).getDeadly()) {
                    repaintTile[counter++] = new Point2(i, j);
                }
                //repaint dynamic tiles
                if (map.getTile(i, j).getDynamic()) {
                    repaintTile[counter++] = new Point2(i, j);
                    map.getTile(i, j).updateDynamicImage();
                }
                //if hero is dead and the tile is the deathpoint draw it
                if (!hero.getAlive() && deathTilePoint == new Point2(i, j)) {
                    repaintTile[counter++] = deathTilePoint;
                }
                //repaint tiles that are under enemy
                for (int x = 0; x < enemyCount; x++) {
                    if (collision.twoEntitiesCollideCurrent(enemy[x], map.getTile(i, j), 0, 0, 0)) {
                        repaintTile[counter++] = new Point2(i, j);
                    }
                }
                if (hero.isAttacking()) {
                    if (collision.twoEntitiesCollideCurrent(hero.getBlow(), map.getTile(i, j), 0, 0, 0)) {
                        repaintTile[counter++] = new Point2(i, j);
                    }
                }
            }
        }
        return repaintTile;
    }
    int animationIterator = 0;

    public void resetIterator() {
        animationIterator = 0;
    }

    public void displayPitAnimation(int iterator) {
        if (iterator % 1 == 0) {
            hero.setLocation(new Point2(-100, -100));
            map.getTile(deathTilePoint).setImage("images/hero/hero_falling" + ++animationIterator + ".png");
        }
    }

    public void displayKillAnimation(int iterator) {
        if (iterator % 3 == 0) {
            hero.setImage("images/hero/hero_killed.png");
        }
    }

    public void displayFireAnimation(int iterator) {
        map.getTile(deathTilePoint).setDynamic(false);
        if (iterator % 3 == 0) {
            hero.setLocation(new Point2(-100, -100));
            map.getTile(deathTilePoint).setImage("images/hero/hero_burning" + ++animationIterator + ".png");
        }
    }

    public int getLives() {
        return lives;
    }

    public int getGoldCount() {
        return gold;
    }

    public void editLives(int edit) {
        lives = lives + edit;

    }

    //if hero is attacking check if blow collides with monster
    public void updateAttack() {
        if (hero.isAttacking()) {
            for (int x = 0; x < enemyCount; x++) {
                if (collision.twoEntitiesCollideCurrent(enemy[x], hero.getBlow(), 10, 10, 10)) {
                    hero.setBlowHit(true);
                    if (enemy[x] instanceof Rat) {
                        enemy[x].setDirection(setRatDirection(x));
                        enemy[x].updateImage();
                        enemy[x].editLives(-1);
                        if (enemy[x].getLives() == 0) {
                            enemy[x].setAlive(false);
                        }
                    }
                }
            }
        } else {
            hero.setBlowHit(false);
        }
    }

    public void updateTimer(int t) {
        timer = t;
    }

    //little method to ease printing text
    private void p(String s) {
        System.out.println(s);
    }
}
