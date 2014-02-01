//Moving characters in Dungeon Quest
import java.awt.*;

public class Character extends MapEntity {

    Warrior.Direction dir;
    int displacement;
    int lives;
    int iter = 0;
    protected int distanceTravelled = 0;
    protected boolean leg = true;
    protected int legWait = 1;
    protected Point2 nextLocation;
    protected boolean alive = true;
    protected Warrior.DeathType death;
    protected String name;

    public Character(String url) {
        super(url);
    }

    public void setDirection(Warrior.Direction d) {
        dir = d;
    }

    public Warrior.Direction getDirection() {
        return dir;
    }

    public void updateImage() {
        switch (dir) {
            case DOWN:
                setLeg("front");
                break;
            case LEFT:
                setLeg("left");
                break;
            case UP:
                setLeg("back");
                break;
            case RIGHT:
                setLeg("right");
                break;
            default:
                break;
        }
    }

    //image when the character isnt moving
    public void setStaticImage() {
        switch (dir) {
            case DOWN:
                setImage("images/" + name + "/" + name + "_front_run1.png");
                break;
            case LEFT:
                setImage("images/" + name + "/" + name + "_left_run1.png");
                break;
            case UP:
                setImage("images/" + name + "/" + name + "_back_run1.png");
                break;
            case RIGHT:
                setImage("images/" + name + "/" + name + "_right_run1.png");
                break;
            default:
                break;
        }
    }

    public void getNextPoint(Warrior.Direction d) {
        switch (d) {
            case DOWN:
                nextLocation = new Point2(location.getX(), location.getY() + displacement);
                dir = d;
                break;
            case LEFT:
                nextLocation = new Point2(location.getX() - displacement, location.getY());
                dir = d;
                break;
            case UP:
                nextLocation = new Point2(location.getX(), location.getY() - displacement);
                dir = d;
                break;
            case RIGHT:
                nextLocation = new Point2(location.getX() + displacement, location.getY());
                dir = d;
                break;
            default:
                nextLocation = location;
                break;
        }

    }

    public Point2 getNextLocation() {
        return nextLocation;
    }

    public void setNextLocation(Point2 p) {
        nextLocation = p;
    }

    private void setLeg(String view) {
        if (legWait++ % 4 == 0) {
            if (leg) {
                setImage("images/" + name + "/" + name + "_" + view + "_run1.png");
                leg = false;
            } else {
                setImage("images/" + name + "/" + name + "_" + view + "_run2.png");
                leg = true;
            }
        }
    }

    public void setAlive(boolean a) {
        alive = a;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setDeath(Warrior.DeathType dt) {
        death = dt;
    }

    public Warrior.DeathType getDeath() {
        return death;
    }

    public void drawCharacter(Graphics g) {
        g.drawImage(img, location.getX() + Warrior.offsetX, location.getY() + Warrior.offsetY, null);
    }

    public int getLives() {
        return lives;
    }

    public void editLives(int edit) {
        lives = lives + edit;
    }

    public void displayDeathAnimation() {
        setImage("images/rat/" + name + "_death.png");
        if (iter == 0) {
            setAlive(false);
        }
        iter++;
    }

    //These functions are to check whether rat is stuck
    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public void addDistanceTravelled() {
        distanceTravelled++;
    }

    public void resetDistanceTravelled() {
        distanceTravelled = 0;
    }
}
