package application.controllers;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundController {
    
    public enum Sounds {
        
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
        private Clip loopClip;

        Sounds(URL path) {
            this.path = path;
        }

        private class CloseClipWhenDone implements LineListener {
            @Override
            public void update(LineEvent event) {
                if (event.getType().equals(LineEvent.Type.STOP)) {
                    Line soundClip = event.getLine();
                    soundClip.close();
                }
            }
        }

        private synchronized Clip getClip() {
            AudioInputStream audioStream;
            Clip clip = null;
            try {
                audioStream = AudioSystem.getAudioInputStream(path);
                clip = AudioSystem.getClip();
                clip.addLineListener(new CloseClipWhenDone());
                clip.open(audioStream);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            loopClip = clip;
            return clip;
        }

        private Clip getLoopClip() {
            return loopClip;
        }
    }

    public static void play(Sounds sound) {
        sound.getClip().start();
    }

    public static void playloop(Sounds sound) {
        Clip loopClip = sound.getClip();
        loopClip.loop(-1);
    }

    public static void stoploop(Sounds sound) {
        sound.getLoopClip().close();
    }
}

