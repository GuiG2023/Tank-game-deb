package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 8/2/24 @ 18:31
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class Animation {
    private float x, y;
    private List<BufferedImage> frames;
    private final int delay = 50;
    private int currentFrame = 0;
    private boolean running = false;
    private long timesinceLastFrameUpdate = 0;

    public Animation(float x, float y, List<BufferedImage> frames) {
        this.x = x - frames.get(0).getWidth() / 2f;
        this.y = y - frames.get(0).getHeight() / 2f;
        this.frames = frames;
        this.running = true;
        this.currentFrame = 0;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (this.timesinceLastFrameUpdate + delay < currentTime) {
            this.currentFrame++;
            if (this.currentFrame == this.frames.size()) {
                this.running = false;
            }
            this.timesinceLastFrameUpdate = currentTime;
        }
    }

    public void render(Graphics g) {
        if (this.running) {
            AffineTransform scaler = AffineTransform.getTranslateInstance(x, y);
            scaler.scale(1.5, 1.5);
            //g.drawImage(this.frames.get(currentFrame), (int) x, (int) y, null);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.frames.get(currentFrame), scaler, null);
        }
    }
}
