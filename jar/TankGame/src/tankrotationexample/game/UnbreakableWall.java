package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 7/10/24 @ 18:19
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class UnbreakableWall extends GameObject {
    private int hitCount;
    public UnbreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }
}
