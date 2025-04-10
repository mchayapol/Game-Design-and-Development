package edu.au.gdd;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var game = new SpaceInvaders();
            game.setVisible(true);
        });
    }
}