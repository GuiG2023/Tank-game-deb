package tankrotationexample.game;

import javax.sound.sampled.Clip;

/**
 * 8/1/24 @ 22:36
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class Sound {
    private Clip clip;
    private int loopCount;

    public Sound(Clip c) {
        this.clip = c;
        this.loopCount = 0;
    }

    public Sound(Clip c, int loopCount) {
        this.clip = c;
        this.loopCount = loopCount;
        this.clip.loop(this.loopCount);
    }

    public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        this.clip.stop();
    }

    public void loopContinue() {
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
