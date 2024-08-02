package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * 7/22/24 @ 10:27
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class Bullet extends GameObject implements Poolable,Updatable,Collidable {

    private float vx;
    private float vy;
    private float angle;
    private int tkID = -1;

    private float R = 4;
    private float ROTATIONSPEED = 1.0f;


    Bullet b;

    private boolean isEnemy;
    private boolean destroyed;

    public Bullet(BufferedImage img) { //for resource pool
        super(0, 0, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.img = img;

    }

    public Bullet(float x, float y, float angle, BufferedImage img) {
        super(x, y, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
        this.img = img;
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


    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
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

    public void update(GameWorld gw) {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        //checkBorder();
        this.hitBox.setLocation((int) x, (int) y);
    }


    @Override
    public void initObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void initObject(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void resetObject() {
        this.x = -5;
        this.y = -5;
    }

    public void setOwner(int id) {
        this.tkID = id;
    }

    public int getOwner() {
        return this.tkID;
    }

    @Override
    public Rectangle getHitBox() {
        return this.hitBox;
    }
}

