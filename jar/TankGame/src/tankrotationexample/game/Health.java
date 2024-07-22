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
public class Health {
    float x,y;
    BufferedImage img;

    public Health(String imgPath) {
        img = ResourceManager.getSprites("health");
    }

    public Health(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
