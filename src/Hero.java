
import java.awt.*;

public class Hero extends Character {

    MapEntity blow = new MapEntity("images/blow3.png");
    boolean attacking = false;
    boolean jumping = false;
    int attackCounter = 0;
    boolean blowHasHit = false;

    public Hero(String url) {
        super(url);
        dir = Warrior.Direction.DOWN;
        name = "hero";
        displacement = 4;

        SIZE_X = 40;
        SIZE_Y = 40;

        blow.SIZE_X = 20;
        blow.SIZE_Y = 24;

        blow.setLocation(0, 0);
    }

    //unique hero class, because hero isnt always moving
    public void setStaticImage() {
        switch (dir) {
            case DOWN:
                setImage("images/hero/hero_front2.png");
                break;
            case LEFT:
                setImage("images/hero/hero_left.png");
                break;
            case UP:
                setImage("images/hero/hero_back.png");
                break;
            case RIGHT:
                setImage("images/hero/hero_right.png");
                break;
            default:
                break;
        }
    }

    public void setBlowLocation() {
        switch (dir) {
            case DOWN:
                blow.setLocation(location.getX() + 10, location.getY() + 38);
                blow.SIZE_Y = 24;
                break;
            case LEFT:
                blow.setLocation(location.getX() - 20, location.getY());
                blow.SIZE_Y = 40;
                break;
            case UP:
                blow.setLocation(location.getX() + 10, location.getY() - 20);
                blow.SIZE_Y = 24;
                break;
            case RIGHT:
                blow.setLocation(location.getX() + 38, location.getY());
                blow.SIZE_Y = 40;
                break;
            default:
                break;
        }
    }

    public MapEntity getBlow() {
        return blow;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean endAttack(int iter) {
        if (iter > 3) {
            attacking = false;
            return true;
        }
        return false;
    }

    public void setAttacking(boolean a) {
        attacking = a;
    }

    public void setBlowHit(boolean h) {
        blowHasHit = h;
    }

    public boolean getBlowHit() {
        return blowHasHit;
    }

    public void setAttackingImg() {
        switch (dir) {
            case DOWN:
                setImage("images/hero/hero_front_attack.png");
                break;
            case LEFT:
                setImage("images/hero/hero_left_attack.png");
                break;
            case UP:
                setImage("images/hero/hero_back_attack.png");
                break;
            case RIGHT:
                setImage("images/hero/hero_right_attack.png");
                break;
            default:
                break;
        }
    }

    public void setJumping(boolean j) {
        jumping = j;
    }

    public void drawCharacter(Graphics g) {
        location.getX();
        g.drawImage(img, location.getX() + Warrior.offsetX, location.getY() + Warrior.offsetY, null);
    }

    public void drawBlow(Graphics g) {
        if (attacking && blowHasHit) {
            g.drawImage(blow.getImage(), blow.getLocation().getX() + Warrior.offsetX, blow.getLocation().getY() + Warrior.offsetY, null);
        }
    }
}
