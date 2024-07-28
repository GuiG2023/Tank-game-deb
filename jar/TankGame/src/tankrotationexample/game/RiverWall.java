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
public class RiverWall extends GameObject {

    public RiverWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
