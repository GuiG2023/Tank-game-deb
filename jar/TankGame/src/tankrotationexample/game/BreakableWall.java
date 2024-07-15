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
public class BreakableWall {
    BufferedImage img;
    public BreakableWall(String imgPath) {
       img = ResourceManager.getSprites("bwall");
    }
}
