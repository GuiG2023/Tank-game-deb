package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.ResourceManager;
import tankrotationexample.ResourcePools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author anthony-pc
 */
public class Tank extends GameObject implements Updatable, Collidable {
    /**
     * handle the collision
     */
    private int tkID;
    private float screen_x;
    private float screen_y;
    private float vx;
    private float vy;
    float angle;

    private float R = 4;
    private float tempSpeed;
    float ROTATIONSPEED = 4.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private boolean shootPressed;

    final long coolDown = 500;
    long timeSinceLastShot = 0;

    //private boolean isEnemy;
    private boolean destroyed = false;
    private boolean shootBoostFlag =false;

    private int lives = 3;
    private int healthPerLife = 4;
    private int currentHealth = healthPerLife;

    private float startX, startY;

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    private boolean multiDirectionalShootingEnabled = false;


    public int getTkID() {
        return tkID;
    }

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.tkID = new Random().nextInt(300);
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.startX = x;
        this.startY = y;
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

    public float safeShootX() {
        return this.x + this.img.getWidth() / 2f;

    }

    public float safeShootY() {
        return this.y + this.img.getHeight() / 2f;
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
            shoot(gw);
//            var p = ResourcePools.getPoolInstance("bullet");
//            p.initObject(safeShootX(), safeShootY(), angle);
//            Bullet b = (Bullet) p;
//            b.setOwner(this.tkID);
//            gw.addGameObject(b);
//            Sound fire = ResourceManager.getSound("fire");
//            fire.setVolumeToMax();
//            fire.play();
//            gw.animations.add(new Animation(x, y, ResourceManager.getAnim("explosion_sm")));
//
        }

        // use flag control power ups?


        centerScreen();
        this.hitBox.setLocation((int) x, (int) y);
//        System.out.println(hitBox.x +"  "+ hitBox.y);
    }

    private void shoot(GameWorld gw) {
        if (multiDirectionalShootingEnabled) {
            multiDirectionShoot(gw);
        } else if (shootBoostFlag) {//to be continue..... new mode
            shootBoost(gw);
        } else {
            normalShoot(gw);
        }
    }

    private void shootBoost(GameWorld gw) {// //to be continue..... new mode
        var p = ResourcePools.getPoolInstance("bullet");
        float[] angles = new float[]{-2,2};
        for (float shootAngle : angles) {
            p.initObject(safeShootX(), safeShootY(), angle);
            Bullet b = new Bullet(safeShootX(), safeShootY(), this.angle + shootAngle, ResourceManager.getSprites("bullet"));
            b.setOwner(this.tkID);
            gw.addGameObject(b);
        }
        Sound fire = ResourceManager.getSound("fire");
        fire.setVolumeToMax();
        fire.play();
        gw.animations.add(new Animation(x, y, ResourceManager.getAnim("explosion_sm")));
    }

    protected void normalShoot(GameWorld gw) {
        var p = ResourcePools.getPoolInstance("bullet");
        p.initObject(safeShootX(), safeShootY(), angle);
        Bullet b = (Bullet) p;
        b.setOwner(this.tkID);
        gw.addGameObject(b);
        Sound fire = ResourceManager.getSound("fire");
        fire.setVolumeToMax();
        fire.play();
        gw.animations.add(new Animation(x, y, ResourceManager.getAnim("explosion_sm")));
    }

    private void multiDirectionShoot(GameWorld gw) {
        var p = ResourcePools.getPoolInstance("bullet");
        float[] angles = new float[]{-15, 0, 15};
        for (float shootAngle : angles) {
            p.initObject(safeShootX(), safeShootY(), angle);
            Bullet b = new Bullet(safeShootX(), safeShootY(), this.angle + shootAngle, ResourceManager.getSprites("bullet"));
            b.setOwner(this.tkID);
            gw.addGameObject(b);
        }
        Sound fire = ResourceManager.getSound("fire");
        fire.setVolumeToMax();
        fire.play();
        gw.animations.add(new Animation(x, y, ResourceManager.getAnim("explosion_sm")));
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

    void moveForwards() {
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

    void checkBorder() {
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
        g.drawImage(this.img, rotation, null);
        g.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g.drawRect((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        int barWidth = 50;
        int barHeight = 5;
        int segmentWidth = barWidth / healthPerLife;
/***
 *
 * draw life bar and lives
 */
        GradientPaint gp = new GradientPaint(x, y - 20, Color.BLACK, x + barWidth, y - 20, Color.RED);
        g.setColor(new Color(50, 50, 50));
        g.fillRect((int) x, (int) (y - 20), barWidth, barHeight);
        g.setPaint(gp);
        g.fillRect((int) x, (int) (y - 20), barWidth, barHeight);
        Color darkGreen = new Color(0, 128, 0);
        g.setColor(darkGreen);
        g.fillRect((int) x, (int) (y - 20), segmentWidth * currentHealth, barHeight);

        g.setColor(Color.WHITE);
        g.drawRect((int) x, (int) (y - 20), barWidth, barHeight);
        g.drawString("Lives: " + lives, x, y - 30);

    }


    //destroy status

    //    public void setSpeed(float speed){
//        this.R = speed
//    }
    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }

    @Override
    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public void stopMovement() {
        this.vx = 0;
        this.vy = 0;
        this.unToggleDownPressed();
        this.unToggleUpPressed();
        this.unToggleLeftPressed();
        this.unToggleRightPressed();


    }

    public void carriedMovement() {
        this.x = x - (float) 0.4;
    }

    public void slowMovement() {
        this.R = 2;
    }

    public void restoreMovement() {
        this.R = 4;
    }

    public void takeDamage() {
        this.currentHealth -= 1;
        if (this.currentHealth <= 0) {
            this.lives -= 1;
            this.currentHealth = healthPerLife;
            if (lives > 0) {
                resetPosition();
            } else {
                destroy();
            }
        }
    }

    public void addLife() {
        this.lives += 1;
    }

    private void resetPosition() {
        this.x = startX;
        this.y = startY;
        this.restoreMovement();

    }

    public void fastMovement() {
        R = 7;
    }

    public void enableMultiDirectionalShooting() {
        multiDirectionalShootingEnabled = true;
        int duration = 30000; // enjoy 30s
        new Timer(duration, new ActionListener() {// refer to chatgpt, should give some credit to it
            public void actionPerformed(ActionEvent e) {
                disableMultiDirectionalShooting();
            }
        }).start();
    }

    public void disableMultiDirectionalShooting() {
        multiDirectionalShootingEnabled = false;
    }

    public void stopMovement2() {
        this.vx = 0;
        this.vy = 0;

        this.x -= this.vx;
        this.y -= this.vy;
    }

    public void applyBoost() {
         shootBoostFlag = true;
    }
}
