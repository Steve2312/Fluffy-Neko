package com.hibiki.graphics;

public class Transitions {
    private static int alpha = 255;
    public static int StateTransition(boolean StateFinish) {
        if(StateFinish) {
            if (alpha < 255) {
                alpha += 3;
            }
        } else {
            if (alpha > 0) {
                alpha -= 3;
            }
        }
        return alpha;
    }
}
