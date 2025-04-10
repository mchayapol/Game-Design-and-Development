package edu.au.gdd;

public interface Global {

    int SCALE_FACTOR = 3; // Scaling factor for sprites

    int BOARD_WIDTH = 716; // Doubled from 358
    int BOARD_HEIGHT = 700; // Doubled from 350
    int BORDER_RIGHT = 60; // Doubled from 30
    int BORDER_LEFT = 10; // Doubled from 5

    int GROUND = 580; // Doubled from 290
    int BOMB_HEIGHT = 10; // Doubled from 5

    int ALIEN_HEIGHT = 24; // Doubled from 12
    int ALIEN_WIDTH = 24; // Doubled from 12
    int ALIEN_INIT_X = 300; // Doubled from 150
    int ALIEN_INIT_Y = 10; // Doubled from 5
    int ALIEN_GAP = 30; // Gap between aliens

    int GO_DOWN = 30; // Doubled from 15
    int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    int CHANCE = 5;
    int DELAY = 17;
    int PLAYER_WIDTH = 30; // Doubled from 15
    int PLAYER_HEIGHT = 20; // Doubled from 10
}