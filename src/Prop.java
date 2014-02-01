
import java.awt.*;

public class Prop extends MapEntity {

    public static enum PropType { KEG, MEAT };
    private int boundaryX = 0;
    private int boundaryY = 0;
    private boolean visible;

    public Prop(String url) {
        super(url);
        setBoundaries();
        SIZE_X = 40;
        SIZE_Y = 40;
        visible = true;
    }

    //this means we dont need separate classes for each object, e.g. Keg.java
    private void setBoundaries() {
        if (url.equalsIgnoreCase("images/map/keg.png")) {
            boundaryX = 20;
            boundaryY = 35;
        } else if (url.equalsIgnoreCase("images/map/meat.png")) {
            boundaryX = 25;
            boundaryY = 30;
        } else if (url.equalsIgnoreCase("images/map/gold.png")) {
            boundaryX = 20;
            boundaryY = 30;
        }
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getBoundaryX() {
        return boundaryX;
    }

    public int getBoundaryY() {
        return boundaryY;
    }

    public void drawProp(Graphics g) {
        if (visible) {
            g.drawImage(img, location.getX() + Warrior.offsetX, location.getY() + Warrior.offsetY, null);
        }
    }
}
