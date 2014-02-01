//Map Editor for Warrior Igor P. (c)
//Make a new file then you can save it

import java.awt.*;
import java.awt.image.BufferStrategy.*;
import java.awt.event.*;
import javax.swing.*;

public class MapEditor extends javax.swing.JFrame {

    static MapPanel mp = new MapPanel();
    static int height = mp.getHeight();
    static int width = mp.getWidth();
    static UtilityPanel up = new UtilityPanel(height);
    static TopPanel tp = new TopPanel(width);

    public MapEditor() {
        super("Warrior Map Editor by Igor P.");

        setIconImage(new ImageIcon("images/icons/editor.png").getImage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        add(tp, BorderLayout.PAGE_START);
        add(up, BorderLayout.LINE_START);
        add(mp, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        clickedLoop();
    }

    public void clickedLoop() {
        while (true) {
            if (up.getUrl() != null) {
                //System.out.println(up.getUrl());
                mp.setNewTileUrl(up.getUrl());
                up.clearUrl();
            }
            if (tp.getAction() != null) {
                if (tp.getAction() == "images/icons/page_white.png") {
                    mp.getMap().createTemplate();
                    repaint();
                }
                if (tp.getAction() == "images/icons/disk.png") {
                    mp.getMap().saveMap(tp.getTextFieldInput());
                    System.out.println("Map saved");
                }
                if (tp.getAction() == "images/icons/folder.png") {
                    mp.getMap().openMap(tp.getTextFieldInput());
                    repaint();
                }
                tp.clearAction();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTLookAndFeel");
        } catch (Exception e) {
        }
        MapEditor me = new MapEditor();
    }
}

//this panel displays the map
class MapPanel extends JPanel {

    static boolean developerView = true;
    Map editMap;
    String newTileUrl = "images/map/rock2.png";
    String rightClickedTileUrl = null;

    public MapPanel() {
        editMap = new Map();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                selectClickedTileCoordinates(e, e.getX(), e.getY());
            }
        });
    }

    public void selectClickedTileCoordinates(MouseEvent e, int xLoc, int yLoc) {
        for (int i = 0; i < editMap.width; i++) {
            for (int j = 0; j < editMap.height; j++) {
                if (xLoc > editMap.getTile(i, j).getLocation().getX() && xLoc < editMap.getTile(i, j).getLocation().getX() + editMap.SIZE && yLoc > editMap.getTile(i, j).getLocation().getY() && yLoc < editMap.getTile(i, j).getLocation().getY() + editMap.SIZE) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        configureNewTile(i, j);
                    } else {
                        newTileUrl = editMap.getTile(i, j).getUrl();
                    }
                }
            }
        }
    }

    public void configureNewTile(int i, int j) {
        editMap.getTile(i, j).setImage(newTileUrl);
        //set deadly ones
        if (newTileUrl.equalsIgnoreCase("images/map/rock_pit.png")) {
            editMap.getTile(i, j).setDeadly(true);
        } else {
            editMap.getTile(i, j).setDeadly(false);
        }
        //set walkable
        if (newTileUrl.equalsIgnoreCase("images/map/rock2.png") || newTileUrl.equalsIgnoreCase("images/map/rock_pit.png") || newTileUrl.equalsIgnoreCase("images/map/fire_pit1.png") || newTileUrl.equalsIgnoreCase("images/map/bones.png")) {
            editMap.getTile(i, j).setWalkable(true);
        } else {
            editMap.getTile(i, j).setWalkable(false);
        }
        //set dynamic
        if (newTileUrl.equalsIgnoreCase("images/map/wall_front_torch1.png") || newTileUrl.equalsIgnoreCase("images/map/fire_pit1.png")) {
            editMap.getTile(i, j).setDynamic(true);
        } else {
            editMap.getTile(i, j).setDynamic(false);
        }
        repaint();
    }

    public void setNewTileUrl(String url) {
        newTileUrl = url;
    }

    public Dimension getPreferredSize() {
        return new Dimension(editMap.width * editMap.SIZE, editMap.height * editMap.SIZE);
    }

    public Map getMap() {
        return editMap;
    }

    public int getHeight() {
        return editMap.height * editMap.SIZE;
    }

    public int getWidth() {
        return editMap.width * editMap.SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
    }

    public void drawObjects(Graphics g) {
        editMap.drawForEditor(g);
    }
}

class UtilityPanel extends JPanel implements ActionListener {

    int h;
    int buttonCounter = 0;
    GridLayout grid = new GridLayout(0, 2, 5, 5);
    JButton[] button = new JButton[50];
    String selectedTileUrl = null;

    public UtilityPanel(int num) {
        h = num;
        setLayout(grid);

        importTile("images/map/rock2.png");
        importTile("images/map/soil.png");
        importTile("images/map/rock_pit.png");
        importTile("images/map/wall_back.png");
        importTile("images/map/wall_corner_ne.png");
        importTile("images/map/wall_corner_nw.png");
        importTile("images/map/wall_corner_se.png");
        importTile("images/map/wall_corner_sw.png");
        importTile("images/map/wall_front.png");
        importTile("images/map/wall_left.png");
        importTile("images/map/wall_right.png");
        importTile("images/map/wall_front_left.png");
        importTile("images/map/wall_front_right.png");
        importTile("images/map/wall_back_left.png");
        importTile("images/map/wall_back_right.png");
        importTile("images/map/wall_front_torch1.png");
        importTile("images/map/fire_pit1.png");
        importTile("images/map/bones.png");
        importTile("images/map/keg.png");
        importTile("images/map/debris.png");
    }

    public void importTile(String url) {
        button[++buttonCounter] = new JButton(new ImageIcon(url));
        button[buttonCounter].setBorderPainted(false);
        button[buttonCounter].setFocusPainted(false);
        button[buttonCounter].setMargin(new Insets(0, 0, 0, 0));
        button[buttonCounter].setContentAreaFilled(false);
        add(button[buttonCounter]);

        //int b = buttonCounter;
        button[buttonCounter].addActionListener(this);
        button[buttonCounter].setActionCommand(url);
    }

    public void actionPerformed(ActionEvent e) {
        selectedTileUrl = e.getActionCommand();
        repaint();
    }

    public String getUrl() {
        return selectedTileUrl;
    }

    public void clearUrl() {
        selectedTileUrl = null;
    }

    public Dimension getPreferredSize() {
        return new Dimension(90, h);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

class TopPanel extends JPanel implements ActionListener {

    int w;
    String selectedAction = null;
    int buttonCounter = 0;
    JButton[] button = new JButton[10];
    JLabel url;
    JTextField input;
    BoxLayout box = new BoxLayout(this, BoxLayout.LINE_AXIS);

    public TopPanel(int num) {
        w = num;

        setLayout(box);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        importTile("images/icons/page_white.png");
        importTile("images/icons/disk.png");
        importTile("images/icons/folder.png");
        url = new JLabel("   Url: ");
        url.setFont(new Font("sansserif", Font.PLAIN, 12));
        add(url);
        input = new JTextField("maps/new.map", 10);
        input.setFont(new Font("sansserif", Font.PLAIN, 12));
        input.setColumns(10);
        add(input);
    }

    public void importTile(String url) {
        button[++buttonCounter] = new JButton(new ImageIcon(url));
        button[buttonCounter].setMargin(new Insets(0, 0, 0, 0));
        button[buttonCounter].setBorderPainted(false);
        button[buttonCounter].setFocusPainted(false);
        button[buttonCounter].setContentAreaFilled(false);
        add(button[buttonCounter]);

        button[buttonCounter].addActionListener(this);
        button[buttonCounter].setActionCommand(url);
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, 35);
    }

    public void actionPerformed(ActionEvent e) {
        selectedAction = e.getActionCommand();
    }

    public String getAction() {
        return selectedAction;
    }

    public void clearAction() {
        selectedAction = null;
    }

    public String getTextFieldInput() {
        return input.getText();
    }
}