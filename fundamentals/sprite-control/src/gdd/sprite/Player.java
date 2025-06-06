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

    public static final int DIR_LEFT = 0;
    public static final int DIR_RIGHT = 1;
    private int facing = 0;

    private static final int ACT_STANDING = 0;
    private static final int ACT_RUNNING = 1;
    private int action = ACT_STANDING;

    private int clip = 0;
    private final Rectangle[] clips = new Rectangle[]{
        new Rectangle(18, 20, 80, 90), // stand still
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
        return clips[clip].height;
    }

    public int getFacing() {
        return facing;
    }

    @Override
    public int getWidth() {
        return clips[clip].width;
    }

    @Override
    public Image getImage() {
        Rectangle bound = clips[clip];
        // TODO this can be cached.
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
        System.out.printf("Player action=%d frame=%d facing=%d\n", action, frame, facing);

        frame++;

        switch (action) {
            case ACT_STANDING:
                if (clip == 1 && frame > 5) {   // blink only one frame
                    frame = 0;
                    clip = 0;
                }
                if (frame > 40) { // blink
                    frame = 0;
                    clip = 1; // blink
                }

                break;
            case ACT_RUNNING:
                if (frame <= 10) {
                    clip = 3;
                } else if (frame <= 20) {
                    clip = 2;
                } else if (frame <= 30) {
                    clip = 3;
                } else if (frame <= 40) {
                    clip = 4;
                } else {
                    clip = 3;
                    frame = 0;
                }

                break;
        }

        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if (action != ACT_RUNNING) {
                // Change of action
                frame = 0;
            }
            action = ACT_RUNNING;
            facing = DIR_LEFT;
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if (action != ACT_RUNNING) {
                // Change of action
                frame = 0;
            }
            action = ACT_RUNNING;
            facing = DIR_RIGHT;
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if (action != ACT_STANDING) {
                // Change of action
                clip = 0;
                frame = 0;
            }
            action = ACT_STANDING;
            facing = DIR_LEFT;
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if (action != ACT_STANDING) {
                // Change of action
                clip = 0;
                frame = 0;
            }
            action = ACT_STANDING;
            facing = DIR_RIGHT;
            dx = 0;
        }
    }
}
