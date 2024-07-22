package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * 7/10/24 @ 18:19
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class Speed {
    float x, y;
    BufferedImage img;

    public Speed(String imgPath) {
        img = ResourceManager.getSprites("speed");
    }

    public Speed(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
