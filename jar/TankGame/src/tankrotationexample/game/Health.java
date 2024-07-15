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
public class Health {
    BufferedImage img;
    public Health(String imgPath) {
        try {
            img = ImageIO.read(Objects.requireNonNull(Health.class.getClassLoader().getResource(imgPath),
                    "Health img is missing"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
