package application.controllers;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundController {
    
    public static enum Sounds {
        
        BORDER(SoundController.class.getResource("/resources/sounds/border.wav")),
        START(SoundController.class.getResource("/resources/sounds/start.wav")),
        MOVE(SoundController.class.getResource("/resources/sounds/move.wav")),
        LAND(SoundController.class.getResource("/resources/sounds/land.wav")),
        LEVELUP(SoundController.class.getResource("/resources/sounds/levelup.wav")),
        SCORE_SMALL(SoundController.class.getResource("/resources/sounds/score_small.wav")),
        SCORE_BIG(SoundController.class.getResource("/resources/sounds/score_big.wav")),
        PAUSE_IN(SoundController.class.getResource("/resources/sounds/pause_in.wav")),
        PAUSE_OUT(SoundController.class.getResource("/resources/sounds/pause_out.wav"));
        
        private URL path;
        private AudioInputStream audioStream;
        private Clip clip;


        Sounds(URL path) {
            this.path = path;
        }

        private Clip getClip() {
            try {
                audioStream = AudioSystem.getAudioInputStream(path);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return clip;
        }
    }

    public static void play(Sounds sounds) {
        sounds.getClip().start();
    }
}

