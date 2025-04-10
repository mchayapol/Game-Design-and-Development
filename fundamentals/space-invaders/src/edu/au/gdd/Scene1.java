package edu.au.gdd.scene;

import edu.au.gdd.sprite.Enemy;
import edu.au.gdd.sprite.Player;
import edu.au.gdd.sprite.Shot;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Scene1 extends JPanel {

    private Dimension d;
    private List<Enemy> enemies;
    private Player player;
    private Shot shot;
    
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;


    public Scene1() {

        initBoard();
        gameInit();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Global.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }


    private void gameInit() {

        enemies = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                var enemy = new Enemy(Global.ALIEN_INIT_X + 18 * j,
                        Global.ALIEN_INIT_Y + 18 * i);
                enemies.add(enemy);
            }
        }

        player = new Player();
        shot = new Shot();
    }

    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Enemy e : enemies) {

            Enemy.Bomb b = e.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {

            g.drawLine(0, Global.GROUND,
                    Global.BOARD_WIDTH, Global.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Global.BOARD_WIDTH / 2 - 30, Global.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Global.BOARD_WIDTH / 2 - 30, Global.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Global.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Global.BOARD_WIDTH / 2);
    }

    private void update() {

        if (deaths == Global.NUMBER_OF_ALIENS_TO_DESTROY) {

            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Enemy enemy : enemies) {

                int enemyX = enemy.getX();
                int enemyY = enemy.getY();

                if (enemy.isVisible() && shot.isVisible()) {
                    if (shotX >= (enemyX)
                            && shotX <= (enemyX + Global.ALIEN_WIDTH)
                            && shotY >= (enemyY)
                            && shotY <= (enemyY + Global.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        enemy.setImage(ii.getImage());
                        enemy.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // enemies

        for (Enemy enemy : enemies) {

            int x = enemy.getX();

            if (x >= Global.BOARD_WIDTH - Global.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Enemy> i1 = enemies.iterator();

                while (i1.hasNext()) {

                    Enemy e2 = i1.next();
                    e2.setY(e2.getY() + Global.GO_DOWN);
                }
            }

            if (x <= Global.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Enemy> i2 = enemies.iterator();

                while (i2.hasNext()) {

                    Enemy e = i2.next();
                    e.setY(e.getY() + Global.GO_DOWN);
                }
            }
        }

        Iterator<Enemy> it = enemies.iterator();

        while (it.hasNext()) {

            Enemy enemy = it.next();

            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > Global.GROUND - Global.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                enemy.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Enemy enemy : enemies) {

            int shot = generator.nextInt(15);
            Enemy.Bomb bomb = enemy.getBomb();

            if (shot == Global.CHANCE && enemy.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(enemy.getX());
                bomb.setY(enemy.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Global.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Global.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Global.GROUND - Global.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
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

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}