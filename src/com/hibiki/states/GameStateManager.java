package com.hibiki.states;


import com.hibiki.levels.LevelState;
import com.hibiki.util.ConfigFile;
import com.hibiki.util.KeyHandler;
import com.hibiki.util.MouseHandler;

import java.awt.*;
import java.io.File;

public class GameStateManager {

    private GameState states[];
    private GameState all_levels[];
    private int[] all_grid_x = {2, 3, 3, 4, 4, 6, 6};
    private int[] all_grid_y = {2, 2, 3, 3, 4, 4, 6};
    private File[] listOfFiles;

    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int PLAY = 2;
    public static final int LEVEL = 3;
    public static final int GALLERY = 4;
    public static final int SETTINGS = 5;


    public GameStateManager() {
        states = new GameState[6];
        states[INTRO] = new IntroState(this);

        // Load all levels
        File levels = new File("images/all_levels");
        listOfFiles = levels.listFiles();
        all_levels = new GameState[listOfFiles.length];

        //states[MENU] = new MenuState(this);
        //states[PLAY] = new PlayState(this);
        //states[LEVEL] = new LevelState(this, all_grid_x[2], all_grid_y[2], listOfFiles[2].toString());
    }

    public void pop(int state) {
        states[state] = null;
    }

    public void add(int state) {
        if (states[state] != null) {
            return;
        }

        if (state == PLAY) {
            states[PLAY] = new PlayState(this);
        }

        if (state == MENU) {
            states[MENU] = new MenuState(this);
        }

        if (state == LEVEL) {
            int x = ConfigFile.getConfigSettings("AT_LEVEL");
            states[LEVEL] = new LevelState(this, all_grid_x[x], all_grid_y[x], listOfFiles[x].toString());
        }

        if (state == GALLERY) {
            states[MENU] = new MenuState(this);
        }

        if (state == SETTINGS) {
            states[MENU] = new MenuState(this);
        }
    }

    public void addAnpop(int state) {
        for(int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i] = null;
            }
        }
        add(state);
    }

    public void update(){
        for(int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].update();
            }
        }
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        for(int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].input(mouse, key);
            }
        }
    }

    public void render(Graphics2D g) {
        for(int i = 0; i < states.length; i++) {
            if (states[i] != null) {
                states[i].render(g);
            }
        }
    }
}
