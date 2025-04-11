package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;
    private int width;
    private int frame = 0;
    
    private static final int ACT_STANDING = 0;
    private static final int ACT_RUNNING = 1;

    private int action = ACT_STANDING;
    private static final int TOTAL_FRAMES = 5;
    private Rectangle[] clips = new Rectangle[]{
        new Rectangle(18, 20, 80, 90),  // stand still
        new Rectangle(110, 20, 80, 90), // stand blink
        new Rectangle(294, 20, 90, 90), // run 1
        new Rectangle(400, 20, 60, 90), // run 2
        new Rectangle(470, 20, 80, 90) // run 3
    };

    public Player() {
        initPlayer();
    }

    public int getFrame() {
        return frame;
    }

    @Override
    public int getHeight() {
        return clips[frame].height;
    }

    @Override
    public int getWidth() {
        return clips[frame].width;
    }
    
    @Override
    public Image getImage() {
        Rectangle bound = clips[frame];
        BufferedImage bImage = toBufferedImage(image);
        return bImage.getSubimage(bound.x, bound.y, bound.width, bound.height);
    }

    private void initPlayer() {
        var ii = new ImageIcon(IMG_PLAYER);
        setImage(ii.getImage());

        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        frame = (frame + 1) % TOTAL_FRAMES;
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}
