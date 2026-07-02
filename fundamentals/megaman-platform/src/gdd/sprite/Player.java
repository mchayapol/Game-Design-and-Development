package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 100;
    private static final int START_Y = 680;

    private static final int JUMP_VELOCITY = -24;
    private static final int GRAVITY = 1;
    private static final int SHOOT_DURATION = 10;

    private int frame = 0;
    private int shootTimer = 0;
    private boolean isFiring = false;

    public static final int DIR_LEFT = 0;
    public static final int DIR_RIGHT = 1;
    private int facing = 0;

    private static final int ACT_STANDING = 0;
    private static final int ACT_RUNNING = 1;
    private static final int ACT_JUMPING = 2;
    private int action = ACT_STANDING;

    private int clipNo = 0;
    private final Rectangle[] clips = new Rectangle[] {
            new Rectangle(20, 21, 76, 88), // 0: stand
            new Rectangle(112, 21, 76, 88), // 1: stand blink
            new Rectangle(204, 21, 76, 88), // 2: run 1
            new Rectangle(296, 29, 88, 80), // 3: run 2
            new Rectangle(400, 21, 56, 88), // 4: run 3
            new Rectangle(20, 229, 88, 108), // 5: jump (rise)
            new Rectangle(140, 229, 96, 108), // 6: jump (fall)
            new Rectangle(132, 129, 88, 88), // 7: shoot (ground) frame 1
            new Rectangle(260, 133, 80, 80), // 8: shoot (ground) frame 2
            new Rectangle(92, 349, 60, 108), // 9: jump+shoot frame 1
            new Rectangle(296, 349, 60, 108), // 10: jump+shoot frame 2

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

    public void act(List<Platform> platforms) {
        frame++;

        if (shootTimer > 0) {
            shootTimer--;
            if (shootTimer == 0) {
                isFiring = false;
            }
        }

        int groundY = groundBelow(platforms);

        if (action != ACT_JUMPING && y < groundY) {
            // walked off the edge of a platform
            action = ACT_JUMPING;
        }

        if (action == ACT_JUMPING) {
            dy += GRAVITY;

            if (dy < 0) {
                int bonkY = ceilingAbove(platforms, y + dy);
                if (bonkY != NO_CEILING) {
                    y = bonkY;
                    dy = 0;
                } else {
                    y += dy;
                }
            } else {
                y += dy;
            }

            if (y >= groundY) {
                y = groundY;
                dy = 0;
                frame = 0;
                action = (dx == 0) ? ACT_STANDING : ACT_RUNNING;
            }
        }

        switch (action) {
            case ACT_STANDING:
                if (isFiring) {
                    clipNo = (frame / 5) % 2 == 0 ? 7 : 8;
                    break;
                }

                if (clipNo == 1 && frame > 5) {
                    frame = 0;
                    clipNo = 0;
                }
                if (frame > 40) {
                    frame = 0;
                    clipNo = 1;
                }

                break;
            case ACT_RUNNING:
                if (isFiring) {
                    clipNo = (frame / 5) % 2 == 0 ? 7 : 8;
                    break;
                }

                if (frame <= 10) {
                    clipNo = 3;
                } else if (frame <= 20) {
                    clipNo = 2;
                } else if (frame <= 30) {
                    clipNo = 3;
                } else if (frame <= 40) {
                    clipNo = 4;
                } else {
                    clipNo = 3;
                    frame = 0;
                }

                break;
            case ACT_JUMPING:
                if (isFiring) {
                    clipNo = (frame / 6) % 2 == 0 ? 9 : 10;
                } else {
                    clipNo = (dy < 0) ? 5 : 6;
                }
                break;
        }

        x += dx;

        if (x <= 2) {
            x = 2;
        }

        int spriteWidth = getWidth();
        if (x >= WORLD_WIDTH - 2 * spriteWidth) {
            x = WORLD_WIDTH - 2 * spriteWidth;
        }
    }

    /**
     * Finds the y coordinate (feet level) of the highest platform surface
     * beneath the player's current x range, falling back to the ground.
     */
    private int groundBelow(List<Platform> platforms) {
        int ground = START_Y;
        int left = x;
        int right = x + getWidth();

        for (Platform platform : platforms) {
            Rectangle bounds = platform.getBounds();
            boolean overlapsX = right > bounds.x && left < bounds.x + bounds.width;
            // Only surfaces at or below the player's current feet can be landed on;
            // a platform overhead must not be mistaken for the ground beneath.
            if (overlapsX && bounds.y >= y && bounds.y < ground) {
                ground = bounds.y;
            }
        }

        return ground;
    }

    private static final int NO_CEILING = Integer.MIN_VALUE;

    /**
     * If rising to prospectiveY would push the player's head into the
     * underside of a platform, returns the feet-level y where the player's
     * head bonks against it instead. Returns NO_CEILING when the path is clear.
     */
    private int ceilingAbove(List<Platform> platforms, int prospectiveY) {
        int left = x;
        int right = x + getWidth();
        int currentTop = y - getHeight();
        int prospectiveTop = prospectiveY - getHeight();

        int bonkY = NO_CEILING;
        for (Platform platform : platforms) {
            Rectangle bounds = platform.getBounds();
            boolean overlapsX = right > bounds.x && left < bounds.x + bounds.width;
            int platformBottom = bounds.y + bounds.height;

            if (overlapsX && currentTop >= platformBottom && prospectiveTop <= platformBottom) {
                int clampedY = platformBottom + getHeight();
                if (bonkY == NO_CEILING || clampedY > bonkY) {
                    bonkY = clampedY;
                }
            }
        }

        return bonkY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if (action != ACT_RUNNING && action != ACT_JUMPING) {
                frame = 0;
            }
            if (action != ACT_JUMPING) {
                action = ACT_RUNNING;
            }
            facing = DIR_LEFT;
            dx = -2;
        } else if (key == KeyEvent.VK_RIGHT) {
            if (action != ACT_RUNNING && action != ACT_JUMPING) {
                frame = 0;
            }
            if (action != ACT_JUMPING) {
                action = ACT_RUNNING;
            }
            facing = DIR_RIGHT;
            dx = 2;
        } else if (key == KeyEvent.VK_SPACE) {
            if (action != ACT_JUMPING) {
                action = ACT_JUMPING;
                dy = JUMP_VELOCITY;
                frame = 0;
            }
        } else if (key == KeyEvent.VK_Z || key == KeyEvent.VK_X) {
            isFiring = true;
            shootTimer = SHOOT_DURATION;
            frame = 0;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if (action != ACT_JUMPING && action != ACT_STANDING) {
                clipNo = 0;
                frame = 0;
            }
            if (action != ACT_JUMPING) {
                action = ACT_STANDING;
            }
            facing = DIR_LEFT;
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if (action != ACT_JUMPING && action != ACT_STANDING) {
                clipNo = 0;
                frame = 0;
            }
            if (action != ACT_JUMPING) {
                action = ACT_STANDING;
            }
            facing = DIR_RIGHT;
            dx = 0;
        }

        if (key == KeyEvent.VK_Z || key == KeyEvent.VK_X) {
            isFiring = false;
            shootTimer = 0;
        }
    }
}
