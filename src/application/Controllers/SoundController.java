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
        PAUSE_OUT(SoundController.class.getResource("/resources/sounds/pause_out.wav")),
        BGM(SoundController.class.getResource("/resources/sounds/bgm.wav"));
        
        private URL path;

        Sounds(URL path) {
            this.path = path;
        }

        private URL getPath() { return path; }
    }


    private Clip loopClip;

    private static Clip getClip(Sounds sound) {
        AudioInputStream audioStream;
        Clip clip = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(sound.getPath());
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return clip;
    }

    public static void play(Sounds sound) {
        getClip(sound).start();
    }

    public void playloop(Sounds sound) {
        loopClip = getClip(sound);
        loopClip.loop(-1);
    }

    public void stoploop() {
        loopClip.stop();
    }
}

