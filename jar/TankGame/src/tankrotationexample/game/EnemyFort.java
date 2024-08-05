package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 7/31/24 @ 23:26
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class EnemyFort extends Tank implements Updatable,Collidable {
    private static final double R = 0.9;
    private static final double MAX_ROTATION_PER_FRAME = 5 ;
    private float vx;
    private float vy;
    private final Tank playerTank;
    private final Tank playerTank2;
    private float detectionRange = 500;

    final long coolDown = 5000;

    public EnemyFort(float x, float y, BufferedImage img, Tank player, Tank playerTank2) {
        super(x, y, 0, 0, 0, img);
        this.playerTank = player;
        this.playerTank2 = playerTank2;
    }

    @Override
    public void update(GameWorld gw) {
        followPlayer();
        followPlayer2();
        autoShoot(gw);
    }


    private void followPlayer() {
        if (playerTank != null && !playerTank.isDestroyed()) {
            float dx = playerTank.getX() - this.getX();
            float dy = playerTank.getY() - this.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < detectionRange) {
                float targetAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                this.angle = graduallyRotate(this.angle, targetAngle);
                this.moveForwards();
                this.moveBackwards();
                this.rotateLeft();
                this.rotateRight();
            }
        }
    }

    private void followPlayer2() {
        if (playerTank2 != null && !playerTank2.isDestroyed()) {
            float dx = playerTank2.getX() - this.getX();
            float dy = playerTank2.getY() - this.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < detectionRange) {
                float targetAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                this.angle = graduallyRotate(this.angle, targetAngle);
                this.moveForwards();
                this.moveBackwards();
                this.rotateLeft();
                this.rotateRight();
            }
        }
    }

    private float graduallyRotate(float currentAngle, float targetAngle) {
        float angleDifference = targetAngle - currentAngle;
        angleDifference = (float) Math.min(angleDifference, MAX_ROTATION_PER_FRAME);
        angleDifference = (float) Math.max(angleDifference, -MAX_ROTATION_PER_FRAME);
        return currentAngle + angleDifference;
    }

    private void setAngle(float angleToPlayer) {
        this.angle = angleToPlayer;
    }

    private void autoShoot(GameWorld gw) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.timeSinceLastShot + this.coolDown) {
            this.timeSinceLastShot = currentTime;
            normalShoot(gw);
        }
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

    @Override
    public Rectangle getHitBox() {
        return this.hitBox;
    }
}

