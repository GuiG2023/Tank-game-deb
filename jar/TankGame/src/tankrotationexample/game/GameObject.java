package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 7/22/24 @ 10:34
 *
 * @ Author : Guiran LIU
 * Description:
 */
public abstract class GameObject {
    /**
     *
     * @param  abastract to type
     */

    protected float x, y; // if private ,need getter and setter
    protected BufferedImage img;

    public static GameObject newInstance(String type, float x, float y) {
        return switch (type) {
            case "9" -> new UnbreakableWall(x, y, ResourceManager.getSprites("ubwall"));
            case "2" -> new BreakableWall(x, y, ResourceManager.getSprites("bwall"));
            case "3" -> new RiverWall(x, y, ResourceManager.getSprites("riverwall"));
            default -> throw new IllegalArgumentException("unsupported type --> %s\n".formatted(type));
        };
    }

    public void drawImage(Graphics g){
        g.drawImage(this.img, (int)x,(int)y, null);
    }
}