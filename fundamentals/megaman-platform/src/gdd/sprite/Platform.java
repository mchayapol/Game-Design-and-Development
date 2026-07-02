package gdd.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Platform {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Color color;

    public Platform(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.white);
        g.drawRect(x, y, width, height);
    }
}
