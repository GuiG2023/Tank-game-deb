package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.ResourceManager;
import tankrotationexample.ResourcePools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author anthony-pc
 */
public class Tank extends GameObject implements Updatable {
    /**
     * handle the collision
     */
    private int tkID;
    private float screen_x;
    private float screen_y;
    private float vx;
    private float vy;
    private float angle;

    private float R = 1;
    private float ROTATIONSPEED = 1.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private boolean shootPressed;

    private final long coolDown = 2000;
    private long timeSinceLastShot = 0;

    private boolean isEnemy;
    private boolean destroyed;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.tkID = new Random().nextInt(300);
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
    }

    public float getScreen_x() {
        return screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void toggleShootPressed() {
        this.shootPressed = true;
    }

    void unToggleShootPressed() {
        this.shootPressed = false;
    }

    public void update(GameWorld gw) {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        long currentTime = System.currentTimeMillis();

        if (this.shootPressed && currentTime > this.timeSinceLastShot + this.coolDown) {
            this.timeSinceLastShot = currentTime;
            var p = ResourcePools.getPoolInstance("bullet");
            p.initObject(x, y, angle);
            Bullet b = (Bullet) p;
            b.setOwner(this.tkID);
            gw.addGameObject((Bullet) p);
//            GameWorld.gObj.add((Bullet) p);
//            this.ammo.add(
//                    new Bullet(x + this.img.getWidth() / 2f,
//                            y + 10 + this.img.getWidth() / 2f,
//                            angle, ResourceManager.getSprites("bullet")));
        }

        // use flag control power ups?


        centerScreen();
        this.hitBox.setLocation((int) x, (int) y);
//        System.out.println(hitBox.x +"  "+ hitBox.y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void centerScreen() {
        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH / 4f;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT / 4f;

        if (this.screen_x < 0) screen_x = 0;
        if (this.screen_y < 0) screen_y = 0;

        if (this.screen_x > GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f) {
            screen_x = GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f;
        }
        if (this.screen_y > GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT) {
            this.screen_y = GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }
    }

    private void checkBorder() {
        if (x < 30) x = 30;
        if (y < 40) y = 40;
        if (x >= GameConstants.WORLD_HEIGHT - 88) {
            x = GameConstants.WORLD_WIDTH - 88;
        }


        if (y >= GameConstants.WORLD_HEIGHT - 115) {
            y = GameConstants.GAME_SCREEN_HEIGHT - 115;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        ((Graphics2D) g).drawImage(this.img, rotation, null);
        ((Graphics2D) g).setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        ((Graphics2D) g).drawRect((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
    }


    //destroy status
    public void destroy() {
        this.destroyed = true;
    }

//    public void setSpeed(float speed){
//        this.R = speed
//    }

}
