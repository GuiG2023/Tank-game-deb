package tankrotationexample.game;

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
    BufferedImage img;
    public Shield(String imgPath) {
        try {
            img = ImageIO.read(Objects.requireNonNull(Shield.class.getClassLoader().getResource(imgPath),
                    "shield img is missing"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
