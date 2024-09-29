import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private ArrayList<Player> players;
    private Level level;

    private int maxPlayers = 4; // Maximum number of players allowed
    private int currentPlayerIndex = 0; // Index to track current player being added

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        addKeyListener(this);

        level = new Level();

        players = new ArrayList<>();

        // create a label to display text
        JLabel l = new JLabel();

        // add text to label
        l.setText("label text");
        initializePlayers(); // Initialize with initial number of players

        timer = new Timer(20, this);
    }

    private void initializePlayers() {
        players.add(new Player(720, 700, Color.MAGENTA, level, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP)); // Player 2
        players.add(new Player(70, 700, Color.BLUE, level, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W)); // Player 1
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Player player : players) {
            player.update();
        }
        checkCollisions();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        level.draw(g);
        for (Player player : players) {
            player.draw(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Player player : players) {
            player.keyPressed(e);
        }
        // Check for key to add new players dynamically
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            if (players.size() < maxPlayers) {
                addNewPlayer();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F2) {
            level.nextLevel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Player player : players) {
            player.keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    private void addNewPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == 1) {
            players.add(new Player(100, 700, Color.GREEN, level, KeyEvent.VK_H, KeyEvent.VK_K, KeyEvent.VK_U)); // Player 3
        } else if (currentPlayerIndex == 2) {
            players.add(new Player(550, 700, Color.CYAN, level, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD8)); // Player 4
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < players.size(); i++) {
            Player p1 = players.get(i);
            if (p1.isTagged() && p1.canTag()) {
                for (int j = 0; j < players.size(); j++) {
                    if (i != j) {
                        Player p2 = players.get(j);
                        if (p1.getX() < p2.getX() + p2.getWidth() &&
                            p1.getX() + p1.getWidth() > p2.getX() &&
                            p1.getY() < p2.getY() + p2.getHeight() &&
                            p1.getY() + p1.getHeight() > p2.getY()) {
                            // Tag p2
                            p2.setTagged(true);
                            p2.resetCooldown();
                            p1.setTagged(false);
                        }
                    }
                }
            }
        }
    }
}
