//class to encapsulate all the technical collision stuff

public class Collisions {

    int propCount;
    Point2 deathTilePoint;
    Prop[] props = new Prop[20];
    Character[] enemy = new Character[20];
    Map map;

    public Collisions(Map map, int propCount, Prop[] props, Character[] enemy) {
        this.map = map;
        this.propCount = propCount;
        this.props = props;
        this.enemy = enemy;
    }

    //true if collisions are, return false if arent. me1 is moving object, me2 is tile, operates on nextpoint
    public boolean twoEntitiesCollideNext(Character me1, MapEntity me2, int x, int yu, int yd) {
        if (((me1.getNextLocation().getX() + x < me2.getLocation().getX() + me2.getSizeX() && me1.getNextLocation().getY() + yu < me2.getLocation().getY() + me2.getSizeY())
                && (me1.getNextLocation().getX() + me1.getSizeX() - x > me2.getLocation().getX() && me1.getNextLocation().getY() + me1.getSizeY() - yd > me2.getLocation().getY()))
                || ((me1.getNextLocation().getX() + x < me2.getLocation().getX() + me2.getSizeX() && me1.getNextLocation().getY() + me1.getSizeY() - yd > me2.getLocation().getY())
                && (me1.getNextLocation().getX() + me1.getSizeX() - x > me2.getLocation().getX() && me1.getNextLocation().getY() + yu < me2.getLocation().getY() + me2.getSizeY()))) {
            return true;
        }
        return false;
    }

    //Operates on current point
    public boolean twoEntitiesCollideCurrent(MapEntity me1, MapEntity me2, int x, int yu, int yd) {
        if (((me1.getLocation().getX() + x < me2.getLocation().getX() + me2.getSizeX() && me1.getLocation().getY() + yu < me2.getLocation().getY() + me2.getSizeY()) && (me1.getLocation().getX() + me1.getSizeX() - x > me2.getLocation().getX() && me1.getLocation().getY() + me1.getSizeY() - yd > me2.getLocation().getY())) || ((me1.getLocation().getX() + x < me2.getLocation().getX() + me2.getSizeX() && me1.getLocation().getY() + me1.getSizeY() - yd > me2.getLocation().getY()) && (me1.getLocation().getX() + me1.getSizeX() - x > me2.getLocation().getX() && me1.getLocation().getY() + yu < me2.getLocation().getY() + me2.getSizeY()))) {
            return true;
        }
        return false;
    }

    public Point2 withEnemies(Hero hero, Character enemy) {
        if (twoEntitiesCollideCurrent(hero, enemy, 10, 20, 15) && hero.getAlive()) {

            hero.setDeath(Warrior.DeathType.ENEMY);
            return new Point2(hero.getLocation().getX(), hero.getLocation().getY());
        }
        return null;
    }

    public boolean withTilesArePresent(Character me) {
        for (int i = 0; i < Map.width; i++) {
            for (int j = 0; j < Map.height; j++) {
                if (map.getTile(i, j).getWalkable() == false) {
                    if (twoEntitiesCollideNext(me, map.getTile(i, j), 5, Logic.Y_UP, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //returns int. 0=no collisions 1=collisions with meat 2=collisions with gold 3=something else
    public int withPropsArePresent(Character me) {
        for (int x = 0; x < propCount; x++) {
            if (twoEntitiesCollideNext(me, props[x], props[x].getBoundaryX(), props[x].getBoundaryY(), 15) && props[x].isVisible()) {
                if (props[x].getUrl().equalsIgnoreCase("images/map/meat.png") && me instanceof Hero) {
                    props[x].setVisible(false);
                    return 1;
                } else if (props[x].getUrl().equalsIgnoreCase("images/map/gold.png") && me instanceof Hero) {
                    props[x].setVisible(false);
                    return 2;
                }
                return 3;
            }
        }
        return 0;
    }

    //returns death location point if true, else null
    public Point2 withDeadlyTilesCurrent(Hero me, Point2 dt) {
        for (int i = 0; i < Map.width; i++) {
            for (int j = 0; j < Map.height; j++) {
                if (map.getTile(i, j).getDeadly() == true) {
                    if (twoEntitiesCollideCurrent(me, map.getTile(i, j), Logic.DEADLY, Logic.Y_DEADLY, 0)) {
                        if (map.getTile(i, j).getUrl().equalsIgnoreCase("images/map/fire_pit2.png") || map.getTile(i, j).getUrl().equalsIgnoreCase("images/map/fire_pit3.png")) {
                            me.setDeath(Warrior.DeathType.FIRE);
                            return new Point2(i, j);
                        }
                    }
                }
            }
        }
        return null;
    }

    public Point2 withDeadlyTiles(Hero me) {
        for (int i = 0; i < Map.width; i++) {
            for (int j = 0; j < Map.height; j++) {
                if (map.getTile(i, j).getDeadly() == true) {
                    //System.out.println(i + " " + j);
                    if (twoEntitiesCollideNext(me, map.getTile(i, j), Logic.DEADLY, Logic.Y_DEADLY, 0)) {
                        if (map.getTile(i, j).getUrl().equalsIgnoreCase("images/map/rock_pit.png")) {
                            me.setDeath(Warrior.DeathType.PIT);
                            return new Point2(i, j);
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean withDeadlyForRat(Character rat) {
        for (int i = 0; i < Map.width; i++) {
            for (int j = 0; j < Map.height; j++) {
                if (map.getTile(i, j).getDeadly() == true && !map.getTile(i, j).getUrl().equalsIgnoreCase("images/map/fire_pit2.png") && !map.getTile(i, j).getUrl().equalsIgnoreCase("images/map/fire_pit3.png")) {
                    if (twoEntitiesCollideNext(rat, map.getTile(i, j), Logic.DEADLY, Logic.Y_DEADLY, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean ratCollides(int x) {
        if (enemy[x].getLocation().getX() < 0 || enemy[x].getLocation().getX() > (map.width - 1) * 40 || enemy[x].getLocation().getY() < 0
                || enemy[x].getLocation().getY() > map.width * 40 || withTilesArePresent(enemy[x])
                || withPropsArePresent(enemy[x]) != 0 || withDeadlyForRat(enemy[x])) {
            return true;
        }
        return false;
    }
}
