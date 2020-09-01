package com.hibiki.states;

import com.hibiki.GamePanel;
import com.hibiki.graphics.Transitions;
import com.hibiki.util.DebugInfo;
import com.hibiki.util.KeyHandler;
import com.hibiki.util.MouseHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayState extends GameState {

    private Image background;

    // transition
    private boolean StateFinish = false;
    private int StateSwitchLocation = 0;
    private int transition_alpha = 255;

    private final GameStateManager gsm;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        try {
            //Background
            background = ImageIO.read(new File("background/mainmenu3.png")).getScaledInstance(GamePanel.width, GamePanel.height, Image.SCALE_SMOOTH);


        } catch (IOException e){
            e.printStackTrace();
        }

        this.gsm = gsm;

    }

    public void update() {
        // transition
        if (Transitions.StateTransition(StateFinish) == 255) {
            gsm.addAnpop(StateSwitchLocation);
        }

        transition_alpha = Transitions.StateTransition(StateFinish);
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        if(!StateFinish) {
            if (key.getKeyCode() == 27) {
                StateSwitchLocation = GameStateManager.MENU;
                StateFinish = true;
            }
        }

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
        // Background Image
        g.drawImage(background, 0, 0, GamePanel.width, GamePanel.height, null);

        // transition
        g.setColor(new Color(255, 255, 255, transition_alpha));
        g.fillRect(0,0, GamePanel.width, GamePanel.height);

        //INFO PANNEL
        DebugInfo.debugRender(g, GamePanel.mouse, GamePanel.key, getClass().getSimpleName());

    }
}
