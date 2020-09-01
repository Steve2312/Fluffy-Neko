package com.hibiki.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundFX {

    double volume = ConfigFile.getConfigSettings("SFX_VOLUME") / 100.0;

    public void playSound(String path){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.setFramePosition(0);
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * (float) volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            clip.start();
            clip.addLineListener(new LineListener() {
                public void update(LineEvent myLineEvent) {
                    if (myLineEvent.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        System.out.println("Clip Closed");
                    }
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
