package tankrotationexample;

import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 7/12/24 @ 16:01
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Resource %s was not found".formatted(path)));
    }
    private static void initSprites() throws IOException {
        ResourceManager.sprites.put("menu",loadSprite("title.png"));
        ResourceManager.sprites.put("t1",loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2",loadSprite("tank2.png"));
        ResourceManager.sprites.put("bwall",loadSprite("bwall.png"));
        ResourceManager.sprites.put("ubwall",loadSprite("ubwall.png"));
        ResourceManager.sprites.put("riverwall",loadSprite("riverwall.png"));
        ResourceManager.sprites.put("health",loadSprite("health.png"));
        ResourceManager.sprites.put("background",loadSprite("Background.bmp"));
        ResourceManager.sprites.put("bullet",loadSprite("bullet.png"));
        ResourceManager.sprites.put("shield",loadSprite("shield3.png"));
        ResourceManager.sprites.put("speed",loadSprite("speed.png"));
        ResourceManager.sprites.put("sand",loadSprite("sand.png"));


    }

    public static void loadAssets()  {
        try {
            initSprites();
        } catch (IOException e) {
            throw new RuntimeException("loading assets failed",e);
        }
    }

    public static BufferedImage getSprites(String key){
        if (!ResourceManager.sprites.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sprites.get(key);
    }

    public static Sound getSound(String key){
        if (!ResourceManager.sounds.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sounds.get(key);
    }

    public static List<BufferedImage> getAnim(String key){
        if (!ResourceManager.animations.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.animations.get(key);
    }

    public static void main(String[] args) throws IOException {
        ResourceManager.loadAssets();
        System.out.println();
    }
}
