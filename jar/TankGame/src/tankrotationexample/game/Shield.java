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
public class Shield {
    float x, y;
    BufferedImage img;

    public Shield(String imgPath) {
        img = ResourceManager.getSprites("shield");
    }

    public Shield(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
}
