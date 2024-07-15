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
public class Speed {
    BufferedImage img;
    public Speed(String imgPath) {
        try {
            img = ImageIO.read(Objects.requireNonNull(Speed.class.getClassLoader().getResource(imgPath),
                    "speed img is missing"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
