//A skeleton walks from one wall to another, vertically forever and ever...

public class Skeleton extends Character {

    public Skeleton(String url) {
        super(url);
        name = "skeleton";
        dir = Warrior.Direction.UP;
        displacement = 2;

        SIZE_X = 40;
        SIZE_Y = 46;
    }
}
