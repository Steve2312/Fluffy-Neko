package com.hibiki.states;

import com.hibiki.GamePanel;
import com.hibiki.graphics.Transitions;
import com.hibiki.graphics.Animations;
import com.hibiki.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuState extends GameState {

    private Image background;
    private Image logo;

    // transition
    private boolean StateFinish = false;
    private int StateSwitchLocation = 0;

    Animations cb = new Animations();
    Animations pb = new Animations();
    Animations gb = new Animations();
    Animations sb = new Animations();
    Animations eb = new Animations();

    private boolean cb_hover = false;
    private boolean pb_hover = false;
    private boolean gb_hover = false;
    private boolean sb_hover = false;
    private boolean eb_hover = false;

    private int cb_color;
    private int pb_color;
    private int gb_color;
    private int sb_color;
    private int eb_color;

    private boolean show_continue = false;

    private int transition_alpha = 255;

    private final GameStateManager gsm;

    SoundFX select_sound = new SoundFX();

    public MenuState(GameStateManager gsm) {
        super(gsm);

        try {
            background = ImageIO.read(new File("background/realbg.png")).getScaledInstance(GamePanel.width, GamePanel.height, Image.SCALE_SMOOTH);
            // Logo Hidden for now
            logo = ImageIO.read(new File("images/logo2.png"));
            logo = logo.getScaledInstance(GamePanel.width / 100 * 20, (logo.getHeight(null) * (GamePanel.width / 100 * 20)) / logo.getWidth(null) , Image.SCALE_SMOOTH);

            Font main_font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Evogria.otf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(main_font);

            if (ConfigFile.getConfigSettings("AT_LEVEL") != 0) {
                show_continue = true;
                System.out.println("READ");
            } else {
                show_continue = false;
            }
        } catch (IOException | FontFormatException e){
            System.out.println(e);
        }
        
        this.gsm = gsm;
    }

    public void update() {
        if (Transitions.StateTransition(StateFinish) == 255) {
            gsm.addAnpop(StateSwitchLocation);
        }

        cb_color = cb.button_color(cb_hover);
        pb_color = pb.button_color(pb_hover);
        gb_color = gb.button_color(gb_hover);
        sb_color = sb.button_color(sb_hover);
        eb_color = eb.button_color(eb_hover);

        transition_alpha = Transitions.StateTransition(StateFinish);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        int keycode = key.getKeyCode();
        int mx = mouse.getX();
        int my = mouse.getY();
        int mb = mouse.getButton();

        if (!StateFinish) {
            if (show_continue && mx >= continueButton.x && mx <= continueButton.width + continueButton.x && my >= continueButton.y && my <= continueButton.y + continueButton.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    StateSwitchLocation = GameStateManager.LEVEL;
                    StateFinish = true;
                }
                cb_hover = true;
            } else {
                cb_hover = false;
            }
            if (mx >= playButton.x && mx <= playButton.width + playButton.x && my >= playButton.y && my <= playButton.y + playButton.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    StateSwitchLocation = GameStateManager.LEVEL;
                    StateFinish = true;
                }
                pb_hover = true;
            } else {
                pb_hover = false;
            }
            //Gallery Button
            if (mx >= galleryButton.x && mx <= galleryButton.width + galleryButton.x && my >= galleryButton.y && my <= galleryButton.y + galleryButton.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    StateSwitchLocation = GameStateManager.GALLERY;
                    StateFinish = true;
                }
                gb_hover = true;
            } else {
                gb_hover = false;
            }
            //Settings Button
            if (mx >= settingsButton.x && mx <= settingsButton.width + settingsButton.x && my >= settingsButton.y && my <= settingsButton.y + settingsButton.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    StateSwitchLocation = GameStateManager.SETTINGS;
                    StateFinish = true;
                }
                sb_hover = true;
            } else {
                sb_hover = false;
            }
            //Exit Button
            if (mx >= exitButton.x && mx <= exitButton.width + exitButton.x && my >= exitButton.y && my <= exitButton.y + exitButton.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    System.exit(0);
                }
                eb_hover = true;
            } else {
                eb_hover = false;
            }
        }

        if (keycode == 123) {
            key.releaseKeyCode();
            if (DebugInfo.show) {
                DebugInfo.show = false;
            } else {
                DebugInfo.show = true;
            }
        }
    }

    private Rectangle continueButton = new Rectangle(30, GamePanel.height - 300, 100, 30);
    private Rectangle playButton = new Rectangle(30, GamePanel.height - 250, 45, 30);
    private Rectangle galleryButton = new Rectangle(30, GamePanel.height - 200, 100, 30);
    private Rectangle settingsButton = new Rectangle(30, GamePanel.height - 150, 100, 30);
    private Rectangle exitButton = new Rectangle(30, GamePanel.height - 100, 45, 30);


    public void render(Graphics2D g) {
        // Background Image
        g.drawImage(background, 140, 0, GamePanel.width, GamePanel.height, null);

        g.setColor(Color.white);
        g.fillRect(0,0,logo.getWidth(null) + 60, GamePanel.height);
        g.drawImage(logo, 30, GamePanel.height / 10, null);

        // Menu Items
        g.setFont(new Font("Evogria", Font.PLAIN, GamePanel.width / 64));

        //g.setColor(new Color(0,0,0,0));
        //g.draw(playButton);
        //g.draw(galleryButton);
        //g.draw(settingsButton);
        //g.draw(exitButton);

        if (show_continue) {
            g.draw(continueButton);
            g.setColor(new Color(cb_color, cb_color, cb_color));
            g.drawString("continue", continueButton.x, continueButton.y + 25);
        }

        g.setColor(new Color(pb_color,pb_color,pb_color));
        g.drawString("play", playButton.x, playButton.y + 25);

        g.setColor(new Color(gb_color, gb_color, gb_color));
        g.drawString("gallery", galleryButton.x, galleryButton.y + 25);

        g.setColor(new Color(sb_color, sb_color, sb_color));
        g.drawString("settings", settingsButton.x, settingsButton.y + 25);

        g.setColor(new Color(eb_color, eb_color, eb_color));
        g.drawString("exit", exitButton.x, exitButton.y + 25);

        //Credit
        g.setColor(new Color(66, 66, 66));
        g.setFont(new Font("Evogria", Font.PLAIN, 13));
        g.drawString("created by steve2312", GamePanel.width - 150, GamePanel.height - 20);

        // Transition
        g.setColor(new Color(255, 255, 255, transition_alpha));
        g.fillRect(0,0, GamePanel.width, GamePanel.height);

        //INFO PANNEL
        DebugInfo.debugRender(g, GamePanel.mouse, GamePanel.key, getClass().getSimpleName());

    }
}

