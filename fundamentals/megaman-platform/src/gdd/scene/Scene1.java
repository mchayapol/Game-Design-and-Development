package gdd.scene;

import static gdd.Global.*;
import gdd.sprite.Platform;
import gdd.sprite.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene1 extends JPanel {

    private static final int TILE_SIZE = 40;
    private static final int TILE_EMPTY = 0;
    private static final int TILE_GROUND = 1;
    private static final int TILE_PLATFORM = 2;

    // Level layout, one row/column per TILE_SIZE pixels. Columns span the
    // full WORLD_WIDTH (twice the viewport), not just the visible screen.
    // 0 = empty, 1 = ground, 2 = platform.
    private static final int[][] LEVEL_MAP = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private Player player;
    private List<Platform> platforms;
    private int cameraX = 0;
    // private Shot shot;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;

    public Scene1() {
        initBoard();
        gameInit();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        timer = new Timer(DELAY, new GameCycle());
        timer.start();

        gameInit();
    }

    private void gameInit() {
        player = new Player();
        platforms = buildPlatforms(LEVEL_MAP);
    }

    /**
     * Converts the tile grid into rectangles for collision and drawing,
     * merging consecutive same-tile runs within a row into one platform.
     */
    private List<Platform> buildPlatforms(int[][] levelMap) {
        List<Platform> result = new ArrayList<>();

        for (int row = 0; row < levelMap.length; row++) {
            int col = 0;
            while (col < levelMap[row].length) {
                int tile = levelMap[row][col];
                if (tile == TILE_EMPTY) {
                    col++;
                    continue;
                }

                int runStart = col;
                while (col < levelMap[row].length && levelMap[row][col] == tile) {
                    col++;
                }

                int tileX = runStart * TILE_SIZE;
                int tileY = row * TILE_SIZE;
                int tileWidth = (col - runStart) * TILE_SIZE;
                Color color = (tile == TILE_GROUND) ? new Color(60, 40, 20) : new Color(100, 100, 100);
                result.add(new Platform(tileX, tileY, tileWidth, TILE_SIZE, color));
            }
        }

        return result;
    }

    private void drawPlatforms(Graphics g) {
        for (Platform platform : platforms) {
            platform.draw(g);
        }
    }

    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            if (player.getFacing() == Player.DIR_RIGHT) {
                g.drawImage(player.getImage(), player.getX(), player.getY()-player.getHeight(), player.getWidth(), player.getHeight(), this);
            } else {
                g.drawImage(player.getImage(), player.getX()+player.getWidth(), player.getY()-player.getHeight(), -player.getWidth(), player.getHeight(), this);
            }
            // g.drawImage(player.getImage(), player.getX(), player.getY()-player.getHeight(), this);
            g.drawRect(player.getX(),player.getY(),10,10);
            g.drawRect(player.getX(),player.getY()-player.getHeight(),player.getWidth(),player.getHeight());
        }
    }

    private void drawHud(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Frame# " + player.getFrame(), 10, 10);
    }

    // private void drawShot(Graphics g) {
    //     if (shot.isVisible()) {
    //         g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    //     }
    // }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.white);

        if (inGame) {

            Graphics2D world = (Graphics2D) g.create();
            world.translate(-cameraX, 0);
            drawPlatforms(world);
            drawPlayer(world);
            // drawShot(world);
            world.dispose();

            drawHud(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            // gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {

        // player
        player.act(platforms);
        updateCamera();

        // shot
        // if (shot.isVisible()) {
        //     int shotX = shot.getX();
        //     int shotY = shot.getY();
        //     for (Enemy enemy : enemies) {
        //         // Collision detection: shot and enemy
        //         int enemyX = enemy.getX();
        //         int enemyY = enemy.getY();
        //         if (enemy.isVisible() && shot.isVisible()
        //                 && shotX >= (enemyX)
        //                 && shotX <= (enemyX + ALIEN_WIDTH)
        //                 && shotY >= (enemyY)
        //                 && shotY <= (enemyY + ALIEN_HEIGHT)) {
        //             var ii = new ImageIcon(IMG_EXPLOSION);
        //             enemy.setImage(ii.getImage());
        //             enemy.setDying(true);
        //             deaths++;
        //             shot.die();
        //         }
        //     }
        //     int y = shot.getY();
        //     y -= 4;
        //     if (y < 0) {
        //         shot.die();
        //     } else {
        //         shot.setY(y);
        //     }
        // }
    }

    private void updateCamera() {
        int targetX = player.getX() + player.getWidth() / 2 - BOARD_WIDTH / 2;
        cameraX = Math.clamp(targetX, 0, WORLD_WIDTH - BOARD_WIDTH);
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            // if (key == KeyEvent.VK_SPACE && inGame && !shot.isVisible()) {
            //     shot = new Shot(x, y);
            // }
        }
    }
}
