package gdd;

import javax.swing.JFrame;

import gdd.scene.Scene1;
import gdd.scene.Title;

public class Game extends JFrame  {

    Title title;
    Scene1 scene1;
    public Game() {
        title = new Title(this);
        scene1 = new Scene1(this);
        initUI();
    }

    private void initUI() {

        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        loadTitle();
    }

    public void loadTitle() {
        getContentPane().removeAll();
        // add(new Title(this));
        add(title);
        title.start();
        revalidate();
        repaint();
    }

    public void loadScene1() {
        getContentPane().removeAll();
        // add(new Scene1(this));
        add(scene1);
        title.stop();
        scene1.start();
        revalidate();
        repaint();
    }
}