//this file contains the map stuff and enemies
//DONT PUT SKELETONS NEXT TO WALLS
import java.awt.*;
import java.io.*;
import java.util.*;

public class Map {

    Tile[][] tiles;
    String name;
    private Point2 heroStartLocation;
    //official mapsize 18x10, meaning
    final static int DEFAULT_WIDTH = 18;
    final static int DEFAULT_HEIGHT = 10;
    //pixel size
    final static int SIZE = 40;
    //characters that roam the map
    Character[] enemy = new Character[20];
    int enemyCounter = -1;
    //props that are stationary
    Prop[] prop = new Prop[20];
    int propCounter = -1;
    public static int width = 0;
    public static int height = 0;

    public Map(String n) {
        openExistingFile(n);
    }

    private void openExistingFile(String n) {
        getMapSize(n);
        System.out.println(width + " " + height);
        tiles = new Tile[width][height];
        initializeTiles();
        setMap(n);
    }

    public void reloadMap(int xIndex, int yIndex) {
        enemyCounter = -1;
        propCounter = -1;
        readMapFile("maps/" + xIndex + "x" + yIndex + ".map");
    }

    public Map() {
        createTemplate();
    }

    public void createTemplate() {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        tiles = new Tile[width][height];
        initializeTiles();
        writeMapFile("maps/template.map");
    }

    private void initializeTiles() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile();
                Point2 tileLocation = new Point2(i * SIZE, j * SIZE);
                tiles[i][j].setLocation(tileLocation);
            }
        }
    }
    //draws whole map

    public void drawMap(Graphics g) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                g.drawImage(tiles[i][j].getImage(), i * SIZE + Warrior.offsetX, j * SIZE + Warrior.offsetY, null);
            }
        }
    }
    //draws only tiles which are requested in point array

    public void drawMap(Graphics g, boolean dev, Point2[] point) {
        int iter = -1;
        while (point[++iter] != null) {
            g.drawImage(tiles[point[iter].getX()][point[iter].getY()].getImage(), point[iter].getX() * SIZE + Warrior.offsetX, point[iter].getY() * SIZE + Warrior.offsetY, null);
        }
    }
    //offset deleted, developer mode set as default

    public void drawForEditor(Graphics g) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                g.drawImage(tiles[i][j].getImage(), i * SIZE, j * SIZE, null);
                g.setColor(Color.RED);
                g.drawRect(i * SIZE, j * SIZE, SIZE, SIZE);
                g.drawString((i + " " + j), i * SIZE, j * SIZE + 12);
            }
        }
    }

    public void saveMap(String url) {
        writeMapFile(url);
    }

    public void openMap(String url) {
        openExistingFile(url);
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public Tile getTile(Point2 p) {
        return tiles[p.getX()][p.getY()];
    }

    public void setMap(String url) {
        name = url;
        readMapFile(url);
    }

    private void writeMapFile(String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write("# " + DEFAULT_WIDTH + " " + DEFAULT_HEIGHT);
            out.write("\n");
            out.write("@ 12 6 hero");
            out.write("\n");
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    out.write(i + " " + j + " " + tiles[i][j].getUrl() + " "
                            + tiles[i][j].getWalkable() + " " + tiles[i][j].getDeadly() + " " + tiles[i][j].getDynamic());
                    out.write("\n");
                }
            }
            out.close();
        } catch (IOException e) {
        }
    }

    private void getMapSize(String url) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(url));
            String str;
            StringTokenizer st = new StringTokenizer(in.readLine());
            st.nextToken();
            width = Integer.parseInt(st.nextToken());
            height = Integer.parseInt(st.nextToken());
        } catch (IOException e) {
        }
    }
    //enter name of file

    private void readMapFile(String url) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(url));
            String str;
            while ((str = in.readLine()) != null) {
                process(str);
            }
            in.close();
        } catch (IOException e) {
        }
        System.out.println("Map loaded");
    }
    //load a tile based on the line

    private void process(String line) {
        int x = -1;
        int y = -1;
        String url = "";
        boolean w = false;
        boolean d = false;
        boolean n = false;

        if (line.charAt(0) != '#' && line.charAt(0) != '@') {
            StringTokenizer st = new StringTokenizer(line);
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());
            url = st.nextToken();
            w = Boolean.parseBoolean(st.nextToken());
            d = Boolean.parseBoolean(st.nextToken());
            n = Boolean.parseBoolean(st.nextToken());

            //set tile based on .map file line
            tiles[x][y].setImage(url);

            tiles[x][y].setWalkable(w);
            tiles[x][y].setDeadly(d);
            tiles[x][y].setDynamic(n);
        }
        if (line.charAt(0) == '@') {
            String tag;

            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());
            tag = st.nextToken();

            if (tag.equalsIgnoreCase("hero")) {
                heroStartLocation = (new Point2((Map.SIZE * x), (Map.SIZE * y)));
            } else if (tag.equalsIgnoreCase("skeleton")) {
                enemy[++enemyCounter] = new Skeleton("images/skeleton/skeleton.png");
                enemy[enemyCounter].setLocation(new Point2((Map.SIZE * x), (Map.SIZE * y)));
            } else if (tag.equalsIgnoreCase("rat")) {
                enemy[++enemyCounter] = new Rat("images/rat/rat.png");
                enemy[enemyCounter].setLocation(new Point2((Map.SIZE * x), (Map.SIZE * y)));
            } else if (tag.equalsIgnoreCase("keg")) {
                prop[++propCounter] = new Prop("images/map/keg.png");
                prop[propCounter].setLocation(new Point2((Map.SIZE * x), (Map.SIZE * y)));
            } else if (tag.equalsIgnoreCase("meat")) {
                prop[++propCounter] = new Prop("images/map/meat.png");
                prop[propCounter].setLocation(new Point2((Map.SIZE * x), (Map.SIZE * y)));
            } else if (tag.equalsIgnoreCase("gold")) {
                prop[++propCounter] = new Prop("images/map/gold.png");
                prop[propCounter].setLocation(new Point2((Map.SIZE * x), (Map.SIZE * y)));
            }
        }
    }

    public Character[] getEnemies() {
        return enemy;
    }

    public int getEnemyCount() {
        return enemyCounter + 1;
    }

    public Prop[] getProps() {
        return prop;
    }

    public int getPropCount() {
        return propCounter + 1;
    }

    public Point2 getHeroStartLocation() {
        return heroStartLocation;
    }
}
