package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.image.BufferedImage;

/**
 * 7/10/24 @ 18:19
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class RiverWall {
    float x,y;
    BufferedImage img;
    public RiverWall(String imgPath) {
       img = ResourceManager.getSprites("riverwall");
    }

    public RiverWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
