package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author anthony-pc
 * <p>
 * update:7.7 add tank 2
 * @author grliu
 */

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1, t2;
    private final Launcher lf;
    private long tick = 0;

    private List<Tank> enemyTanks;

    private final int MaxEnemies = 5;
    private BufferedImage enemyImg;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
        this.enemyTanks = new ArrayList<>();
    }

    @Override
    public void run() {
        //world.getSubimage();
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank1
                this.t2.update(); // update tank2

                for (Tank enemyTank : enemyTanks) {
                    enemyTank.update(); // update enemy tanks
                }
                checkEnemyTankCount(); // check and create enemy if needed
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */

                //when to end the game?
//                if (t1.died && t2.died){
//                    return;
////                }
                //if all enemies are destoryed

//                if (tick>500){ //end the game
//                    this.lf.setFrame("end");
//                    return;
//                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame() {//reset all the resource
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
        this.t2.setX(500);
        this.t2.setX(500);

        // Reset enemy tanks
        enemyTanks.clear();
        for (int i = 0; i < 5; i++) {
            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
            enemyTanks.add(new Tank(x, y, 0, 0, 0, enemyImg));
        }
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {// inti 2 players tank1 & tank2 with different pic and position
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                GameConstants.GAME_SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        BufferedImage
                t1img = null,
                t2img = null,
                wall = null,
                bWall = null,
                bullet = null,
                rocket = null,
                speed = null,
                health = null,
                shield = null;// add more img
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1.png"),
                            "Could not find tank1.png")
            );
            t2img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2.png"),
                            "Could not find tank2.png")
            );
            enemyImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("enemyTank1.png"),
                            "Could not find enemyTank.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprites("t1"));
        t2 = new Tank(500, 500, 0, 0, (short) 0, ResourceManager.getSprites("t2"));

        /*
         *
         P1 control: wasd & space
         P2 control: keyboard arrows & p(key)
         */

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_P);

        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);

        // Initialize enemy tanks
        enemyTanks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {  // init 5 enemy tanks
            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
            enemyTanks.add(new Tank(x, y, 0, 0, 0, enemyImg));
        }

    }

    private void checkEnemyTankCount() {
        // 移除被消灭的敌方坦克
        Iterator<Tank> iterator = enemyTanks.iterator();
        while (iterator.hasNext()) {
            Tank enemyTank = iterator.next();
//            if (enemyTank.isDestroyed()) {
//                iterator.remove();
//            }
        }

        // 如果敌方坦克数量少于最大值，则生成新的敌方坦克
        while (enemyTanks.size() < MaxEnemies) {
            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
            enemyTanks.add(new Tank(x, y, 0, 0, 0, enemyImg));
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.drawImage(ResourceManager.getSprites("background"), 0, 0, this.getWidth(), this.getHeight(), this);
//        buffer.setColor(Color.black);
// dbuffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        for (Tank enemyTank : enemyTanks) {
            enemyTank.drawImage(buffer);
        }
        g2.drawImage(world, 0, 0, null);
    }
}