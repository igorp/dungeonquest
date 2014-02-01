//A rat walks randomly
public class Rat extends Character {
    int ratAttackCounter;
    boolean ratBiting;

    public Rat(String url) {
        super(url);
        name = "rat";
        dir = Warrior.Direction.UP;
        displacement = 3;
        lives = 9;

        SIZE_X = 40;
        SIZE_Y = 40;
    }

    public void setBitingImage() {
        setImage("images/rat/rat_left_attack.png");
    }

    public void addAttackTime() {
        ratAttackCounter++;
    }

    public int getAttackTime() {
        return ratAttackCounter;
    }

    public void resetAttackTimer() {
        ratAttackCounter = 0;
    }
}
