package com.hibiki.util;

import com.hibiki.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private static int key = 0;

    public KeyHandler(GamePanel game){
        game.addKeyListener(this);
    }

    public int getKeyCode() {
        return key;
    }

    public void releaseKeyCode() {
        key = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        key = 0;
    }
}
