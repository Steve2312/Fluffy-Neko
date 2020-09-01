package com.hibiki.util;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.lang.management.ManagementFactory;

import com.hibiki.GamePanel;
import com.sun.management.OperatingSystemMXBean;

public class DebugInfo {

    public static boolean show = false;

    public static void debugRender(Graphics g, MouseHandler mouse, KeyHandler key, String ClassName) {
        if(show) {
            String cords = "Mouse: x: " + String.valueOf(mouse.getX()) + " y: " + String.valueOf(mouse.getY()) + " b: " + String.valueOf(mouse.getButton());
            String keycodes = "Keycode: " + String.valueOf(key.getKeyCode());

            //INFO PANNEL
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(Color.black);
            g.drawString("FPS: " + String.valueOf(GamePanel.fps), GamePanel.width - 150, 20);
            g.drawString("State: " + ClassName, GamePanel.width - 150, 35);
            g.drawString(cords, GamePanel.width - 150, 50);
            g.drawString(keycodes, GamePanel.width - 150, 65);
            g.drawString("Ticks: " + String.valueOf(GamePanel.ticks), GamePanel.width - 150, 80);
        }
    }
}
