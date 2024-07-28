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

    public UnbreakableWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
