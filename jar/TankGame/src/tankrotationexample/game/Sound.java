package tankrotationexample.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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

    public void setVolume(float level) {
        FloatControl volume = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue((float) Math.log10(level));
    }
    public void setVolumeToMax() {
        FloatControl volume = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        float maxValue = volume.getMaximum();
        volume.setValue(maxValue);
    }


}
