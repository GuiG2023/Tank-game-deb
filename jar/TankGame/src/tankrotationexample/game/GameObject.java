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
private static ResourcePool<Bullet> bulletResourcePool = new ResourcePool<>("bullet",Bullet.class,500);
    protected float x, y; // if private ,need getter and setter
    protected BufferedImage img;
    protected Rectangle hitBox;

    protected boolean hasCollided = false;

//    private static int ID = 0;

    public GameObject(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        hitBox = new Rectangle((int) x,(int) y,this.img.getWidth(),this.img.getHeight());
    }

    public static GameObject newInstance(String type, float x, float y) {
        return switch (type) {
            case "9" -> new UnbreakableWall(x, y, ResourceManager.getSprites("ubwall"));
            case "2" -> new BreakableWall(x, y, ResourceManager.getSprites("bwall"));
            case "3" -> new RiverWall(x, y, ResourceManager.getSprites("riverwall"));
            case "4" -> new Sand(x, y, ResourceManager.getSprites("sand"));
            case "5" -> new Health(x, y, ResourceManager.getSprites("health"));
            case "6" -> new ShootEnhance(x, y, ResourceManager.getSprites("se"));
            case "7" -> new Speed(x, y, ResourceManager.getSprites("speed"));
            case "10" -> new UnbreakableWall2(x, y, ResourceManager.getSprites("bw2"));
            default -> throw new IllegalArgumentException("unsupported type --> %s\n".formatted(type));
        };
    }

    public void drawImage(Graphics2D g){
        g.drawImage(this.img, (int)x,(int)y, null);
    }

    public Rectangle getHitbox(){
        return this.hitBox.getBounds();
    }

    public boolean getHasCollided() {
        return this.hasCollided;
    }
    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }




//    public int getId() {
//        return this.id;
//    }

}