package com.hibiki;

import com.hibiki.states.GameStateManager;
import com.hibiki.util.ConfigFile;
import com.hibiki.util.KeyHandler;
import com.hibiki.util.MouseHandler;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{

    public static int width;
    public static int height;

    public static int fps;
    public static int ticks;

    private Thread thread;
    private boolean running = false;
    private VolatileImage img;
    private Graphics2D g;

    public static MouseHandler mouse;
    public static KeyHandler key;

    private GameStateManager gsm;

    public GamePanel(int width, int height) {
        GamePanel.width = width;
        GamePanel.height = height;
        //Sets JPanels Size
        setPreferredSize(new Dimension(width, height));
        // Allows JPanel to have input when the JFrame is made
        setFocusable(true);
        requestFocus();
    }

    public void addNotify(){
        super.addNotify();

        if(thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }

    }

    public void init() {
        // Loading ConfigFile
        ConfigFile.load_config_file();
        running = true;

        img = createVolatileImage(width, height);
        g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        mouse = new MouseHandler(this);
        key = new KeyHandler(this);

        gsm = new GameStateManager();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("audio/bg4.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * (float) ConfigFile.getConfigSettings("MUSIC_VOLUME") / 100) + gainControl.getMinimum();
            gainControl.setValue(gain);


            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        init();

        //ticks
        double firstTickTime = System.nanoTime();
        double lastTickTime = System.nanoTime();
        final double tick_target = 64.0;
        double time_tick_update = 1000000000 / tick_target;

        //updates
        double tickCount = 0;

        //frames
        double lastFrameTime = System.nanoTime();
        double lastFrameTimeUpdate = System.nanoTime();
        final double fps_target = 240.0;
        int frameCount = 0;

        while(running) {
            long now = System.nanoTime();
            if (now - lastTickTime > time_tick_update) {
                update();
                input(mouse, key);
                tickCount += (now - lastTickTime) / time_tick_update;
                lastTickTime = now;
            }

            if (now - firstTickTime > 1000000000) {
                ticks = (int) tickCount;
                tickCount = 0;
                firstTickTime = now;
            }

            if(now - lastFrameTime > 1000000000 / fps_target) {
                render();
                draw();
                frameCount++;
                lastFrameTime = now;
            }

            // Update FrameRate per second
            if (now - lastFrameTimeUpdate > 1000000000) {
                fps = frameCount;
                frameCount = 0;
                lastFrameTimeUpdate = now;
            }

        }
    }


    public void update(){
        gsm.update();
    }

    public void input(MouseHandler mouse, KeyHandler key){
        gsm.input(mouse, key);
    }

    public void render(){
        if (g != null) {
            super.paint(g);
            // Make Font not pixeltated!
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            gsm.render(g);
        }
    }

    public void draw() {
        Graphics g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();
    }

}
