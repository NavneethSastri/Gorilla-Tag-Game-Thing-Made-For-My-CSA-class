import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("TAG Platformer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);
        
        gamePanel.startGame();
    }
}