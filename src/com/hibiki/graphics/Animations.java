package com.hibiki.graphics;

public class Animations {
    //Menu Buttons
    int button_color = 66;

    public int button_color(boolean on_hover) {
        if (on_hover) {
            if (button_color < 150) {
                button_color += 6;
            }
        } else {
            if (button_color > 66) {
                button_color -= 6;
            }
        }
        return button_color;
    }

    //Selected puzzle
    int puzzle_selected_alpha = 255;
    boolean puzzle_selected_alpha_done = false;

    public int puzzle_selected_alpha(boolean selected) {
        if (selected) {
            if (puzzle_selected_alpha_done) {
                if (puzzle_selected_alpha < 180) {
                    puzzle_selected_alpha += 20;
                } else {
                    puzzle_selected_alpha += 10;
                }

                if (puzzle_selected_alpha > 240) {
                    puzzle_selected_alpha_done = false;
                }

            } else {
                if (puzzle_selected_alpha > 70) {
                    puzzle_selected_alpha -= 2;
                } else {
                    puzzle_selected_alpha--;
                }
                if (puzzle_selected_alpha < 30) {
                    puzzle_selected_alpha_done = true;
                }
            }
        } else {
            puzzle_selected_alpha = 255;
            puzzle_selected_alpha_done = false;
        }

        return puzzle_selected_alpha;
    }

    int next_level_color = 255;
    boolean next_level_done = false;
    public int next_level_button(boolean show, boolean hover) {
        if (hover) {
            if (next_level_color > 70) {
                next_level_color -= 2;
            } else {
                if (next_level_color > 30) {
                    next_level_color--;
                }
            }
        } else if (show) {
            if (next_level_done) {
                if (next_level_color < 100) {
                    next_level_color += 20;
                } else {
                    next_level_color += 10;
                }

                if (next_level_color > 160) {
                    next_level_done = false;
                }

            } else {
                if (next_level_color > 70) {
                    next_level_color -= 2;
                } else {
                    next_level_color--;
                }
                if (next_level_color < 30) {
                    next_level_done = true;
                }
            }
        }

        return next_level_color;
    }

    int back_button_color = 255;
    boolean back_button_done = false;
    public int back_button(boolean show) {
        if (show) {
            if (back_button_done) {
                if (back_button_color < 80) {
                    back_button_color += 20;
                } else {
                    back_button_color += 10;
                }

                if (back_button_color > 120) {
                    back_button_done = false;
                }

            } else {
                if (back_button_color > 70) {
                    back_button_color -= 2;
                } else {
                    back_button_color--;
                }
                if (back_button_color < 30) {
                    back_button_done = true;
                }
            }
        } else {
            back_button_color = 30;
            back_button_done = false;
        }

        return back_button_color;
    }
}
