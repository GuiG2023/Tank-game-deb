package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 8/4/24 @ 20:40
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class UnbreakableWall2 extends GameObject implements Collidable {
    private int hitCount;
    public UnbreakableWall2(float x, float y, BufferedImage img) {
        super(x, y, img);
        this.hitCount = 0;
    }

    @Override
    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public void increaseHitCount() {
        this.hitCount++;
        if (this.hitCount >= 10) {
            this.setHasCollided(true);
        }
    }

    public boolean isDestroyed() {
        return this.hitCount >= 10;
    }
}
