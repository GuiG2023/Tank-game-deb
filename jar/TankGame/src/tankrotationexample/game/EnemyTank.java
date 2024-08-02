package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 7/31/24 @ 23:26
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class EnemyTank extends Tank implements Updatable,Collidable {
    private static final double R = 0.9;
    private static final double MAX_ROTATION_PER_FRAME = 5 ;
    private float vx;
    private float vy;
    private final Tank playerTank;
    private float detectionRange = 300;

    public EnemyTank(float x, float y, BufferedImage img, Tank player) {
        super(x, y, 0, 0, 0, img);
        this.playerTank = player;
    }

    @Override
    public void update(GameWorld gw) {
        followPlayer();
        autoShoot();
    }


    private void followPlayer() {
        if (playerTank != null && !playerTank.isDestroyed()) {
            float dx = playerTank.getX() - this.getX();
            float dy = playerTank.getY() - this.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < detectionRange) {
                float targetAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                this.angle = graduallyRotate(this.angle, targetAngle);  // 平滑旋转
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

    private void autoShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.timeSinceLastShot + this.coolDown) {
            this.timeSinceLastShot = currentTime;
//            shoot();
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

