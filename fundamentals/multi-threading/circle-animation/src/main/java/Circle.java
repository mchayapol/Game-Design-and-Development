import java.awt.Color;
import java.awt.Graphics;

public class Circle {
    private int x;
    private int y;
    private int radius;
    private Color color;
    private String name = "Circle";

    public Circle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void moveDown() {
        this.move(2);
        // System.out.println(name + " moved down to y: " + y);
    }

    public void move(int deltaY) {
        this.y += deltaY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g.setColor(Color.WHITE);
        g.drawString(name, x - radius / 2, y + radius / 2);
    }
}