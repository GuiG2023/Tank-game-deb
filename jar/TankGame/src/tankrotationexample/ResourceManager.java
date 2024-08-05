package tankrotationexample;

import tankrotationexample.game.GameWorld;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

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
    private final static Map<String, Integer> animInfo = new HashMap<>() {{
        put("explosion_lg", 7);
        put("explosion_sm", 6);
    }};

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        GameWorld.class.getClassLoader().getResource(path),
                        "Resource %s was not found".formatted(path)));
    }

    private static Sound loadSound(String path) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Sound %s was not found".formatted(path))
        );
        Clip c = AudioSystem.getClip();
        c.open(ais);
        return new Sound(c);
    }

    private static void initSprites() throws IOException {
        ResourceManager.sprites.put("menu", loadSprite("title.png"));
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("bwall", loadSprite("bwall.png"));
        ResourceManager.sprites.put("ubwall", loadSprite("ubwall.png"));
        ResourceManager.sprites.put("riverwall", loadSprite("riverwall.png"));
        ResourceManager.sprites.put("health", loadSprite("health.png"));
        ResourceManager.sprites.put("background", loadSprite("Background.bmp"));
        ResourceManager.sprites.put("bullet", loadSprite("bullet.png"));
        ResourceManager.sprites.put("se", loadSprite("shell.png"));
        ResourceManager.sprites.put("speed", loadSprite("speed.png"));
        ResourceManager.sprites.put("sand", loadSprite("sand.png"));
        ResourceManager.sprites.put("bw2", loadSprite("wall1.png"));


    }

    public static void loadAssets() {
        try {
            initSprites();
            loadSounds();
            loadAnims();
        } catch (IOException e) {
            throw new RuntimeException("loading assets failed", e);
        }
    }

    private static void loadAnims() {
        String baseFormat = "%s/%s_%04d.png";
        ResourceManager.animInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> t = new ArrayList<>(frameCount);
            try {
                for (int i = 1; i < frameCount; i++) {
                    String spritePath = String.format(baseFormat, animationName, animationName, i);

                    t.add(loadSprite(spritePath));
                }
                ResourceManager.animations.put(animationName,t);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void loadSounds() {
        try {
            ResourceManager.sounds.put("theme", loadSound("bgm/theme.wav"));
            ResourceManager.sounds.put("fire", loadSound("bgm/Explosion_fire.wav"));
            ResourceManager.sounds.put("wallBroken", loadSound("bgm/Explosion_med.wav"));
            ResourceManager.sounds.put("tankBroken", loadSound("bgm/Explosion_large.wav"));
            ResourceManager.sounds.put("start", loadSound("bgm/start.wav"));
            ResourceManager.sounds.put("end", loadSound("bgm/end.wav"));
            ResourceManager.sounds.put("moving", loadSound("bgm/moving.wav"));
            ResourceManager.sounds.put("pickup1", loadSound("bgm/Battle City SFX (15).wav"));
            ResourceManager.sounds.put("pickup2", loadSound("bgm/Battle City SFX (14).wav"));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getSprites(String key) {
        if (!ResourceManager.sprites.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sprites.get(key);
    }

    public static Sound getSound(String key) {
        if (!ResourceManager.sounds.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sounds.get(key);
    }

    public static List<BufferedImage> getAnim(String key) {
        if (!ResourceManager.animations.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.animations.get(key);
    }

    public static void main(String[] args) throws IOException {
        loadAnims();
        System.out.println();
    }

}
