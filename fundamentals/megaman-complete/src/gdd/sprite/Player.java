package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 100;
    private static final int START_Y = GROUND;
    // private int width;
    private int frame = 0;
    private boolean isFiring = false;

    public static final int DIR_LEFT = 0;
    public static final int DIR_RIGHT = 1;
    private int facing = DIR_RIGHT;

    private static final String ACT_IDLE = "IDLE";
    private static final String ACT_IDLE_BLINK = "IDLE_BLINK";
    private static final String ACT_SHOOT = "SHOOT";
    private static final String ACT_RUN = "RUN";
    private static final String ACT_RUN_SHOOT = "RUN_SHOOT";
    private static final String ACT_JUMP = "JUMP";
    private static final String ACT_JUMP_SHOOT = "JUMP_SHOOT";

    private String action = ACT_IDLE;

    private int clipNo = 0;
    private final Rectangle[] clips = new Rectangle[]{
        new Rectangle(18, 20, 80, 90), // 0: stand still
        new Rectangle(110, 20, 80, 90), // 1: stand blink
        new Rectangle(294, 20, 90, 90), // 2: run 1
        new Rectangle(400, 20, 60, 90), // 3: run 2
        new Rectangle(470, 20, 80, 90), // 4: run 3
        new Rectangle(138, 230, 100, 110), // 5: jump 1, no firing
        new Rectangle(18, 230, 100, 110), // 6: jump 2, firing
        new Rectangle(128,124,124,94), // 7: stand Shoot
        new Rectangle(248,120,118,94), // 8: run shoot 1
        new Rectangle(372,120,118,94), // 9: run shoot 2
        new Rectangle(486,120,118,94), // 10: run shoot 3
    };

    public Player() {
        initPlayer();
    }

    public int getFrame() {
        return frame;
    }

    @Override
    public int getHeight() {
        return clips[clipNo].height;
    }

    public int getFacing() {
        return facing;
    }

    @Override
    public int getWidth() {
        return clips[clipNo].width;
    }

    @Override
    public Image getImage() {
        Rectangle bound = clips[clipNo];
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
        // System.out.printf("Player action=%d frame=%d facing=%d (%d,%d)\n", action, frame, facing, x,y);
        System.out.println("Player act: " + action);

        frame++;

        switch (action) {
            case ACT_JUMP_SHOOT:
                clipNo = 6;
                if (frame > 20) {
                    dy = JUMP_DOWN_DELTA;
                }
                if (y + dy > START_Y) {
                    // Player has landed
                    if (dx != 0) {
                        // If player was moving, change action to running
                        action = ACT_RUN;
                    } else {
                        // If player was not moving, change action to standing
                        action = ACT_IDLE;
                    }

                    dy = 0;
                }
                break;
            case ACT_JUMP:
                clipNo = 5;
                if (frame > 20) {
                    dy = JUMP_DOWN_DELTA;
                }
                if (y + dy > START_Y) {
                    // Player has landed
                    if (dx != 0) {
                        // If player was moving, change action to running
                        action = ACT_RUN;
                    } else {
                        // If player was not moving, change action to standing
                        action = ACT_IDLE;
                    }

                    dy = 0;
                }
                break;

            case ACT_IDLE:
                if (frame > 40) { // blink
                    frame = 0;
                    action = ACT_IDLE_BLINK;
                } else {
                    clipNo = 0; // stand still
                }

                break;
            case ACT_IDLE_BLINK:
                if (frame > 10) {
                    frame = 0;
                    action = ACT_IDLE; // back to idle
                } else {
                    clipNo = 1; // stand blink
                }

                break;
            case ACT_SHOOT:
                clipNo = 7; // stand shoot
                break;
            
            case ACT_RUN:
            case ACT_RUN_SHOOT:
                if (frame <= 10) {
                    // clipNo = 3;
                    clipNo = (action == ACT_RUN_SHOOT) ? 9 : 3;
                } else if (frame <= 20) {
                    // clipNo = 2;
                    clipNo = (action == ACT_RUN_SHOOT) ? 8 : 2;
                } else if (frame <= 30) {
                    // clipNo = 3;
                    clipNo = (action == ACT_RUN_SHOOT) ? 9 : 3;
                } else if (frame <= 40) {
                    // clipNo = 4;
                    clipNo = (action == ACT_RUN_SHOOT) ? 10 : 4;
                } else {
                    // clipNo = 3;
                    clipNo = (action == ACT_RUN_SHOOT) ? 9 : 3;
                    frame = 0;
                }

                break;

        }

        x += dx;
        y += dy;

        if (x <= BORDER_LEFT) {
            x = BORDER_LEFT;
        } else if (x >= BOARD_WIDTH - BORDER_RIGHT * width) {
            x = BOARD_WIDTH - BORDER_LEFT * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (action) {
            case ACT_IDLE:
                switch (key) {
                    case KeyEvent.VK_ENTER:
                        action = ACT_SHOOT;
                        isFiring = true;
                        frame = 0;
                        break;
                    case KeyEvent.VK_SPACE:
                        action = ACT_JUMP;
                        dy = JUMP_UP_DELTA;
                        frame = 0;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (action != ACT_RUN) {
                            // if he is not running, start from frame 0
                            frame = 0;
                        }
                        action = ACT_RUN;
                        facing = DIR_LEFT;
                        dx = -2;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (action != ACT_RUN) {
                            // if he is not running, start from frame 0
                            frame = 0;
                        }
                        action = ACT_RUN;
                        facing = DIR_RIGHT;
                        dx = 2;
                        break;

                    default:
                        break;
                }
                break;

            case ACT_RUN:
                if (key == KeyEvent.VK_ENTER) {
                    action = ACT_RUN_SHOOT;
                    isFiring = true;
                    frame = 0;
                } else if (key == KeyEvent.VK_SPACE) {
                    action = ACT_JUMP;
                    dy = JUMP_UP_DELTA;
                    frame = 0;
                }
                break;

            case ACT_JUMP:
                if (key == KeyEvent.VK_ENTER) {
                    action = ACT_JUMP_SHOOT;
                    isFiring = true;
                    // frame = 0;   // continue from ACT_JUMP
                } else if (key == KeyEvent.VK_LEFT) {
                    // Start running left
                    facing = DIR_LEFT;
                    dx = -2;
                } else if (key == KeyEvent.VK_RIGHT) {
                    // Start running right
                    facing = DIR_RIGHT;
                    dx = 2;
                }
                break;

            default:
            // Do nothing
        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (action) {
            case ACT_SHOOT:
                if (key == KeyEvent.VK_ENTER) {
                    // stop shooting
                    action = ACT_IDLE;
                    isFiring = false;
                    frame = 0;
                }
                break;

            case ACT_RUN:
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                    // Stop running
                    action = ACT_IDLE;
                    isFiring = false;
                    frame = 0;
                    dx = 0;
                }
                break;
            case ACT_RUN_SHOOT:
                if (key == KeyEvent.VK_ENTER) {
                    // stop shooting
                    action = ACT_RUN;
                    isFiring = false;
                    frame = 0;
                }
                break;

            case ACT_JUMP_SHOOT:
                if (key == KeyEvent.VK_ENTER) {
                    action = ACT_JUMP;
                    isFiring = false;
                    frame = 0;
                }
                break;

            default:
            // Do nothing
        }

    }
}
