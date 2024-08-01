package tankrotationexample.game;

import java.awt.image.BufferedImage;

/**
 * 7/31/24 @ 23:26
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class EnemyTank extends Tank implements Updatable {
    private final Tank playerTank;
    private float detectionRange = 3000;

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
                float angleToPlayer = (float) Math.toDegrees(Math.atan2(dy, dx));
                this.setAngle(angleToPlayer);
                this.moveForwards();
            }
        }
    }

    private void setAngle(float angleToPlayer) {
    }

    private void autoShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.timeSinceLastShot + this.coolDown) {
            this.timeSinceLastShot = currentTime;
            this.toggleShootPressed();
            this.unToggleShootPressed();
        }
    }
}

