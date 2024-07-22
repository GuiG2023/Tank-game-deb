package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.image.BufferedImage;

/**
 * 7/10/24 @ 18:19
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class UnbreakableWall {
    float x,y;
    BufferedImage img;
    public UnbreakableWall(String imgPath) {
       img = ResourceManager.getSprites("ubwall");
    }

    public UnbreakableWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
