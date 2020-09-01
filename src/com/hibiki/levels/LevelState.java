package com.hibiki.levels;

import com.hibiki.GamePanel;
import com.hibiki.graphics.Animations;
import com.hibiki.states.GameState;
import com.hibiki.states.GameStateManager;
import com.hibiki.graphics.Transitions;
import com.hibiki.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LevelState extends GameState {

    Image background;
    int puzzle_x;
    int puzzle_y;
    int puzzle_width;
    int puzzle_height;
    BufferedImage playfield;

    //puzzle pieces
    ArrayList<BufferedImage> puzzle_pieces = new ArrayList<BufferedImage>();
    ArrayList<Integer> puzzle_pieces_x = new ArrayList<Integer>();
    ArrayList<Integer> puzzle_pieces_y = new ArrayList<Integer>();
    ArrayList<Integer> puzzle_pieces_order = new ArrayList<Integer>();

    int grid_x = 0;
    int grid_y = 0;
    String path = "";

    boolean s_one = false;
    boolean s_two = false;
    int option_one = 0;
    int option_two = 0;

    boolean StateFinish = false;
    int StateSwitchLocation = 0;

    Animations puzzle_animation = new Animations();
    int puzzle_selected_alpha = 255;

    int transition_alpha = 255;

    static int border = 255;

    boolean jsonupdated = false;

    Animations nb = new Animations();
    private boolean nb_show = false;
    private boolean nb_hover = false;
    private int nb_color = 30;

    Animations bb = new Animations();
    private boolean bb_hover = false;
    private int bb_color = 30;

    private final GameStateManager gsm;

    SoundFX select_sound = new SoundFX();

    public LevelState(GameStateManager gsm, int grid_x, int grid_y, String path) {
        super(gsm);

        this.grid_x = grid_x;
        this.grid_y = grid_y;
        this.path = path;

        try {
            //Background
            background = ImageIO.read(new File(path)).getScaledInstance(GamePanel.width, GamePanel.height, Image.SCALE_SMOOTH);
            border = 255;
            // Get puzzle from background
            BufferedImage puzzle = new BufferedImage(background.getWidth(null), background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics graphics_puzzle = puzzle.getGraphics();
            graphics_puzzle.drawImage(background, 0 , 0, null);

            //Calculate Image Border
            puzzle_x = 384 * GamePanel.width / 1920;
            puzzle_y = 108 * GamePanel.height / 1080;

            //Calculate Image Width and Height
            puzzle_width = GamePanel.width - ((384 * GamePanel.width / 1920) * 2);
            puzzle_height =  GamePanel.height - ((108 * GamePanel.height / 1080) * 2);

            // Crop image
            puzzle = puzzle.getSubimage(puzzle_x, puzzle_y, puzzle_width, puzzle_height);

            //Create playfield
            playfield = new BufferedImage(puzzle_width, puzzle_height, BufferedImage.TYPE_INT_RGB);

            //print all points where image needs to be placed
            for(int x = 0; x < grid_x; x++) {
                for (int y = 0; y < grid_y; y++) {
                    int x_piece = puzzle_width / grid_x * x;
                    int y_piece = puzzle_height / grid_y * y;

                    // Create all the pieces and put the x,y and bufferimage in ArrayList
                    puzzle_pieces.add(puzzle.getSubimage(x_piece, y_piece, puzzle_width / grid_x, puzzle_height / grid_y));
                    puzzle_pieces_x.add(x_piece);
                    puzzle_pieces_y.add(y_piece);

                }
            }

            // Image positions
            for(int i = 0; i < grid_x * grid_y; i++){
                puzzle_pieces_order.add(i);
            }

            // Pieces shuffle
            int seed = 3;
            Collections.shuffle(puzzle_pieces, new Random(seed));
            Collections.shuffle(puzzle_pieces_order, new Random(seed));

        } catch (IOException e){
            e.printStackTrace();
        }

        this.gsm = gsm;

    }

    public void update() {
        if (Transitions.StateTransition(StateFinish) == 255) {
            gsm.addAnpop(StateSwitchLocation);
        }

        nb_color = nb.next_level_button(nb_show, nb_hover);

        bb_color = bb.back_button(bb_hover);

        puzzle_selected_alpha = puzzle_animation.puzzle_selected_alpha(s_one);

        transition_alpha = Transitions.StateTransition(StateFinish);

        //Core of game
        if (s_two) {
            Collections.swap(puzzle_pieces, option_one, option_two);
            Collections.swap(puzzle_pieces_order, option_one, option_two);
            option_one = 0;
            option_two = 0;
            s_one = false;
            s_two = false;
        }

        // Check if puzzle is solved
        if (check_sort(puzzle_pieces_order)) {
            if (border > 0) {
                border -= 5;
            }

            nb_show = true;

            if(!jsonupdated) {
                jsonupdated = true;
                if (ConfigFile.getConfigSettings("AT_LEVEL") < 6) {
                    ConfigFile.updateConfigSetting("AT_LEVEL", ConfigFile.getConfigSettings("AT_LEVEL") + 1);
                }
            }
        }

    }

    // method to check the solved state
    public static boolean check_sort(ArrayList<Integer> arr) {
        for (int i = 0; i < arr.size() - 1; i++) {
            if (arr.get(i) > arr.get(i+1)) {
                return false;
            }
        }
        return true;
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        int keycode = key.getKeyCode();
        int mx = mouse.getX();
        int my = mouse.getY();
        int mb = mouse.getButton();
        if(!StateFinish) {
            if (keycode == 27) {
                key.releaseKeyCode();
                StateSwitchLocation = GameStateManager.MENU;
                StateFinish = true;
                select_sound.playSound("audio/esc.wav");
            }

            if (mx > 30 && mx < 30 + 20 && my > 30 && my < 30 + 20) {
                if(mb == 1) {
                    StateSwitchLocation = GameStateManager.MENU;
                    StateFinish = true;
                    select_sound.playSound("audio/esc.wav");
                }
                bb_hover = true;
            } else {
                bb_hover = false;
            }

            if (!check_sort(puzzle_pieces_order)) {
                for (int i = 0; i < puzzle_pieces_x.size(); i++) {
                    int px = puzzle_pieces_x.get(i);
                    int py = puzzle_pieces_y.get(i);
                    if (mx > px + puzzle_x && mx < px + puzzle_x + puzzle_width / grid_x && my > py + puzzle_y && my < py + puzzle_y + puzzle_height / grid_y && mb == 1) {
                        if (s_one) {
                            option_two = i;
                            s_two = true;
                            select_sound.playSound("audio/puzzle_swap.wav");
                        } else {
                            option_one = i;
                            s_one = true;
                            select_sound.playSound("audio/select_puzzle.wav");
                        }
                        mouse.updateButton(-1);
                    } else if (mx < puzzle_x && mouse.getButton() == 1 || mx > puzzle_x + puzzle_width && mb == 1 || my < puzzle_y && mb == 1 || my > puzzle_y + puzzle_height && mb == 1) {
                        option_one = 0;
                        s_one = false;
                    }
                }
            }
            //Next Level Button
            if (mx >= nextLevel.x && mx <= nextLevel.width + nextLevel.x && my >= nextLevel.y && my <= nextLevel.y + nextLevel.height) {
                if (mb == 1) {
                    select_sound.playSound("audio/menuhit.wav");
                    StateSwitchLocation = GameStateManager.LEVEL;
                    StateFinish = true;
                }
                nb_hover = true;
            } else {
                nb_hover = false;
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

    private final int rectw = 30;
    private final int recth = 10;
    private final int triaw = 20;
    private final int triah = 15;
    private final int[] tsp = {GamePanel.width - rectw - 50, GamePanel.height + recth - 50};
    private final Rectangle nextLevel = new Rectangle(tsp[0], tsp[1] - triah / 2, rectw + triaw, recth + triah);

    public void render(Graphics2D g) {
        // Background Image
        g.drawImage(background, 0, 0, GamePanel.width, GamePanel.height, null);

        Graphics2D gpf = playfield.createGraphics();
        for(int i = 0; i < puzzle_pieces.size(); i++) {
            BufferedImage p = puzzle_pieces.get(i);
            int px = puzzle_pieces_x.get(i);
            int py = puzzle_pieces_y.get(i);

            gpf.drawImage(p, px, py, p.getWidth(), p.getHeight(), null);
            if (option_one == i && s_one) {
                gpf.setColor(new Color(255,255,255, puzzle_selected_alpha));
                gpf.fillRect(puzzle_pieces_x.get(i), puzzle_pieces_y.get(i), puzzle_pieces.get(i).getWidth(), puzzle_pieces.get(i).getHeight());
            }
        }

        //Borders
        Graphics2D graphics_border = gpf;
        graphics_border.setStroke(new BasicStroke(3));
        graphics_border.setColor(new Color(255,255,255,border));
        // Horizontal borders
        for(int i = 0; i <= grid_x; i++){
            int pw = puzzle_pieces.get(i).getWidth();
            graphics_border.drawLine(pw * i, 0, pw * i, puzzle_height);
            if (i == grid_x) {
                graphics_border.drawLine(pw * i - 1, 0, pw * i - 1, puzzle_height);
            }
        }
        // Vertical borders
        for(int i = 0; i <= grid_y; i++){
            int ph = puzzle_pieces.get(i).getHeight();
            graphics_border.drawLine(0, ph * i, puzzle_width, ph * i);
            if (i == grid_y) {
                graphics_border.drawLine(0, ph * i - 1, puzzle_width, ph * i - 1);
            }
        }

        // Draw playfield
        g.drawImage(playfield, puzzle_x, puzzle_y, null);

        g.setColor(new Color(bb_color,bb_color,bb_color));
        g.fillOval(30, 30, 20, 20);
        //Arrow
        if(nb_show) {
            g.setColor(new Color(nb_color, nb_color, nb_color));
            //g.draw(nextLevel);
            g.fillRect(tsp[0], tsp[1], rectw, recth);
            int[] point1 = {tsp[0] + rectw, tsp[1] - triah / 2};
            int[] point2 = {tsp[0] + rectw, tsp[1] + recth + triah / 2};
            int[] point3 = {tsp[0] + rectw + triaw, tsp[1] + recth / 2};
            int[] x = {point1[0], point2[0], point3[0]};
            int[] y = {point1[1], point2[1], point3[1]};
            g.fillPolygon(x, y, 3);
        }
        // Transition
        g.setColor(new Color(255, 255, 255, transition_alpha));
        g.fillRect(0, 0, GamePanel.width, GamePanel.height);
        //INFO PANNEL
        DebugInfo.debugRender(g, GamePanel.mouse, GamePanel.key, getClass().getSimpleName());
    }
}
