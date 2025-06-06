
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private List<Circle> circles;

    public GamePanel() {
        this.circles = new ArrayList<>();
        // for (int i = 0; i < 5; i++) {
        //     circles.add(new Circle(100 * (i + 1), 0, 50, Color.RED));
        // }

        // Create and start threads for each circle
    }

    public void addCircle(Circle circle) {
        circles.add(circle);
    }

    public List<Circle> getCircles() {
        return circles;
    }

    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            for (Circle circle : circles) {
                circle.draw(g);
            }
        } catch (java.util.ConcurrentModificationException e) {
        }
    }
}
