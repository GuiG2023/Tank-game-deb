package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * 7/10/24 @ 18:19
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class BreakableWall extends GameObject implements Collidable {


    public BreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public Rectangle getHitBox() {
        return this.hitBox;
    }
}
