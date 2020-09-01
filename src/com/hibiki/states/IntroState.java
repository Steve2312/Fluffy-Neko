package com.hibiki.states;

import com.hibiki.GamePanel;
import com.hibiki.graphics.Transitions;
import com.hibiki.util.DebugInfo;
import com.hibiki.util.KeyHandler;
import com.hibiki.util.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IntroState extends GameState {

    private final GameStateManager gsm;

    private Image author_icon;
    private BufferedImage buffImage;

    // transition
    private boolean StateFinish = false;
    private int StateSwitchLocation = 0;
    private int elapsed_intro_time = 0;
    private int show_intro_time = 200;

    private int transition_alpha = 255;

    public IntroState(GameStateManager gsm) {
        super(gsm);

        try {
            Font main_font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Evogria.otf"));

            // GIF Image
            author_icon = new ImageIcon(String.valueOf(new File("images/author.gif"))).getImage();
            buffImage = new BufferedImage(author_icon.getWidth(null), author_icon.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(main_font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        this.gsm = gsm;

    }

    public void update() {
        //transition
        if (elapsed_intro_time == show_intro_time) {
            StateSwitchLocation = GameStateManager.MENU;
            StateFinish = true;
        } else {
            elapsed_intro_time++;
        }

        if (Transitions.StateTransition(StateFinish) == 255) {
            gsm.addAnpop(StateSwitchLocation);
        }

        transition_alpha = Transitions.StateTransition(StateFinish);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if (key.getKeyCode() == 123) {
            key.releaseKeyCode();
            if (DebugInfo.show) {
                DebugInfo.show = false;
            } else {
                DebugInfo.show = true;
            }
        }
    }

    public void render(Graphics2D g) {
        // Background Color
        g.setColor(Color.white);
        g.fillRect(0,0, GamePanel.width, GamePanel.height);
        // Text
        g.setFont(new Font("Evogria", Font.PLAIN, 20));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(66 ,66 ,66));
        String intro_text = "game is still under development!";
        g.drawString(intro_text, (GamePanel.width - fm.stringWidth(intro_text)) / 2, GamePanel.height / 2 + 75);

        //Author GIF
        Graphics ggif = buffImage.getGraphics();
        ggif.setClip(new Ellipse2D.Float(0, 0, buffImage.getWidth(), buffImage.getHeight()));
        ggif.drawImage(author_icon, 0, 0, null);
        g.drawImage(buffImage, GamePanel.width / 2 - 100, GamePanel.height / 2 - 175, 200, 200, null);


        // Transition
        g.setColor(new Color(255, 255, 255, transition_alpha));
        g.fillRect(0,0, GamePanel.width, GamePanel.height);

        //INFO PANNEL
        DebugInfo.debugRender(g, GamePanel.mouse, GamePanel.key, getClass().getSimpleName());
    }
}
