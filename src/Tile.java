
public class Tile extends MapEntity {

    private boolean walkable;
    private boolean deadly;
    private boolean dynamic;
    private Point2 tileLocation;
    private String noPNGUrl;

    public Tile(String url) {
        super(url);
        walkable = false;
    }

    public Tile() {
        super("images/map/soil.png");
        walkable = false;
        deadly = false;
        dynamic = false;
    }

    public void setWalkable(boolean w) {
        walkable = w;
    }

    public boolean getWalkable() {
        return walkable;
    }

    public void setDeadly(boolean d) {
        deadly = d;
    }

    public boolean getDeadly() {
        return deadly;
    }

    public void setDynamic(boolean d) {
        if (d) {
            noPNGUrl = url.substring(0, url.length() - 5);
        }
        dynamic = d;
    }

    public boolean getDynamic() {
        return dynamic;
    }
    private boolean torchShow = true;
    private boolean firePitShow = true;
    private boolean animateFire = true;
    private int firePitCounter = 1;
    private int dynamicCounter = 1;

    //only for dynamic images
    public void updateDynamicImage() {
        dynamicCounter++;
        if (noPNGUrl.equalsIgnoreCase("images/map/wall_front_torch")) {
            animateTorch();
        }
        if (noPNGUrl.equalsIgnoreCase("images/map/fire_pit")) {
            animateFirePit();
        }
    }

    public void animateTorch() {
        if (dynamicCounter % 6 == 0) {
            if (torchShow) {
                setImage(noPNGUrl + "2.png");
                torchShow = false;
            } else {
                setImage(noPNGUrl + "1.png");
                torchShow = true;
            }
        }
    }

    public void animateFirePit() {
        if (firePitShow) {
            if (firePitCounter++ % 3 == 0) {
                if (animateFire) {
                    setImage(noPNGUrl + "2.png");
                    animateFire = false;
                } else {
                    setImage(noPNGUrl + "3.png");
                    animateFire = true;
                }
            }
        }
        if (dynamicCounter % 20 == 0) {
            if (firePitShow) {
                setDeadly(false);
                setImage(noPNGUrl + "1.png");
                firePitShow = false;
            } else {
                setDeadly(true);
                setImage(noPNGUrl + "2.png");
                firePitShow = true;
            }
        }
    }

    public String toString() {
        return new String(location.getX() + " " + location.getY() + " " + getUrl() + " " + getWalkable() + " " + getDeadly());
    }
}
