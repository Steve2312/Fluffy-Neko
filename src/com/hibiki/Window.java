package com.hibiki;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame{
    private boolean isFullScreen = false;

    private int screenWidth = 1280;
    private int screenHeight = 720;

    public Window() {
        try {
            Image icon = ImageIO.read(new File("icon.png"));
            setIconImage(icon);

        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("Fluffy Neko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIgnoreRepaint(false);
        setUndecorated(isFullScreen);
        pack();
        setLocationRelativeTo(this);
        setResizable(false);
        setVisible(true);
    }

    public void addNotify() {
        super.addNotify();
        if(isFullScreen) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenWidth = screenSize.width;
            screenHeight = screenSize.height;
        }
        setContentPane(new GamePanel(screenWidth, screenHeight));
    }
}
