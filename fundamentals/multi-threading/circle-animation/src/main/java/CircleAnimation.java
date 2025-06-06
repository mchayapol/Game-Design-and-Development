
import java.awt.Color;

import javax.swing.JFrame;

public class CircleAnimation {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game with Multithreading");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);

        // Main Thread to repaint the frame periodically
        new Thread(() -> {
            while (true) {
                try {
                    gamePanel.repaint();
                    // System.out.println("x");
                    Thread.sleep(50); // Control the repaint speed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        for (int i = 0; i < 18; i++) {
            Circle circle = new Circle(20 + i * 40, 40, 20, Color.RED);
            circle.setName("" + i);
            gamePanel.addCircle(circle);

            CircleThread circleThread = new CircleThread(circle);
            new Thread(circleThread).start();
        }
    }
}
