
import java.awt.event.*;

// Keyboard input
public class GameKey implements KeyListener {

    public boolean key_right = false;
    public boolean key_left = false;
    public boolean key_up = false;
    public boolean key_down = false;
    public boolean key_x = false;
    public boolean key_r = false;
    public boolean key_space = false;

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_DOWN) {
            key_down = true;
        }
        if (e.getKeyCode() == e.VK_UP) {
            key_up = true;
        }
        if (e.getKeyCode() == e.VK_LEFT) {
            key_left = true;
        }
        if (e.getKeyCode() == e.VK_RIGHT) {
            key_right = true;
        }
        if (e.getKeyCode() == e.VK_X) {
            key_x = true;
        }
        if (e.getKeyCode() == e.VK_R) {
            key_r = true;
        }
        if (e.getKeyCode() == e.VK_SPACE) {
            key_space = true;
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_DOWN) {
            key_down = false;
        }
        if (e.getKeyCode() == e.VK_UP) {
            key_up = false;
        }
        if (e.getKeyCode() == e.VK_LEFT) {
            key_left = false;
        }
        if (e.getKeyCode() == e.VK_RIGHT) {
            key_right = false;
        }
        if (e.getKeyCode() == e.VK_SPACE) {
            key_space = false;
        }
    }
}
