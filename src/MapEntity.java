//Map Entities are the stuff that appear on screen in the game.
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class MapEntity {

    protected int SIZE_X = 40;
    protected int SIZE_Y = 40;
    static Hashtable<String, BufferedImage> images = new Hashtable<String, BufferedImage>();
    public int size;
    protected BufferedImage img;
    protected Point2 location;
    protected String url;

    public MapEntity(String url) {
        this.url = url;
        img = loadImage(url);
    }

    public MapEntity() {
    }

    public MapEntity(int x, int y) {
        location = new Point2(x, y);
    }

    public String displayInfo() {
        return new String("URL: " + url + " location: " + location);
    }

    public BufferedImage getImage() {
        return img;
    }

    public void setImage(String url) {
        this.url = url;
        img = loadImage(url);
    }

    public static BufferedImage loadImage(String url) {
        BufferedImage image = images.get(url);
        if (image == null) {
            try {
                image = ImageIO.read(new File(url));
                images.put(url, image);
            } catch (IOException e) {
            }
        }
        return image;
    }

    public String getUrl() {
        return url;
    }

    public Point2 getLocation() {
        return location;
    }

    public void setLocation(Point2 p) {
        location = p;
    }

    public void setLocation(int x, int y) {
        location = new Point2(x, y);
    }

    public int getSizeX() {
        return SIZE_X;
    }

    public int getSizeY() {
        return SIZE_Y;
    }
}
