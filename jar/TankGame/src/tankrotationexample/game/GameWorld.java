package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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
    ArrayList<GameObject> gObj = new ArrayList<>(1000);

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
        this.resetGame();
        try {
            while (true) {
                this.tick++;
                for (int i = this.gObj.size()-1; i>=0 ; i--) {
                    if (this.gObj.get(i) instanceof Updatable u){
                        u.update(this);
                    }else {
                        break;
                    }
                }
                for (Tank enemyTank : enemyTanks) {
                    enemyTank.update(this); // update enemy tanks
                }
//                for (GameObject obj : gObj) { // for debugging
//                    System.out.println(obj);
//                }
                this.checkCollision();


//                checkEnemyTankCount(); // check and create enemy if needed
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

    private void checkCollision() {
        for (int i = 0; i < this.gObj.size(); i++) {
            GameObject object1 = this.gObj.get(i);
            for (int j = i + 1; j < this.gObj.size(); j++) {
                GameObject object2 = this.gObj.get(j);

                if (object1.getHitbox().intersects(object2.getHitbox())) {
//                    System.out.println("Collision Detected");
                    handleCollision(object1, object2);
                }
            }
        }
        this.gObj.removeIf(GameObject::getHasCollided);
    }

    private void handleCollision(GameObject obj1, GameObject obj2) {

        if (obj1 instanceof Bullet && obj2 instanceof BreakableWall) {
            obj1.setHasCollided(true);
            obj2.setHasCollided(true);
        } else if (obj2 instanceof Bullet && obj1 instanceof BreakableWall) {
            obj2.setHasCollided(true);
            obj1.setHasCollided(true);
        }
        System.out.println("after handled");
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {//reset all the resource
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(500);
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
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                GameConstants.WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
            enemyImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("enemyTank1.png"),
                            "Could not find enemyTank.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        int row = 0;
        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResourceAsStream("Map/game_map.csv")
                )
        );

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            while (mapReader.ready()) {
                String line = mapReader.readLine();
                String[] objs = line.split(",");
                System.out.println(Arrays.toString(objs));
                for (int col = 0; col < objs.length; col++) {
                    String gameItem = objs[col];
                    if (Objects.equals(gameItem, "0")) continue;
                    this.gObj.add(GameObject.newInstance(gameItem, col * 32, row * 32));
                }
                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        t1 = new Tank(300, 500, 0, 0, (short) 0, ResourceManager.getSprites("t1"));
        t2 = new Tank(500, 500, 0, 0, (short) 0, ResourceManager.getSprites("t2"));
        this.gObj.add(t1);
        this.gObj.add(t2);
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
        for (int i = 0; i < 5; i++) {  // init 5 enemy tanks
            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
            enemyTanks.add(new Tank(x, y, 0, 0, 0, enemyImg));
        }

        //add obstacle
        // ubwall


    }

    private void renderFloor(Graphics buffer) {
        BufferedImage floor = ResourceManager.getSprites("background");
        for (int i = 0; i < GameConstants.WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        for (int i = 0; i < this.gObj.size(); i++) {
            this.gObj.get(i).drawImage(buffer);
        }
        //buffer.drawImage(ResourceManager.getSprites("background"), 0, 0, this.getWidth(), this.getHeight(), this);
//        buffer.setColor(Color.black);
// dbuffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        List<GameObject> copy = new ArrayList<>(gObj);
        copy.forEach(go -> go.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        for (Tank enemyTank : enemyTanks) {
            enemyTank.drawImage(buffer);
        }
        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);
//        g2.drawImage(world, 0, 0, null);
    }


    static double scaleFactor = .20;

    private void displayMiniMap(Graphics2D onScreenPanel) {
        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        double mmx = GameConstants.GAME_SCREEN_WIDTH / 2. - (GameConstants.WORLD_WIDTH * scaleFactor) / 2.;
        double mmy = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.WORLD_HEIGHT * scaleFactor);
        AffineTransform scaler = AffineTransform.getTranslateInstance(mmx, mmy);
        scaler.scale(scaleFactor, scaleFactor);
        onScreenPanel.drawImage(mm, scaler, null);

    }

    private void displaySplitScreen(Graphics2D onScreenPanel) {
        BufferedImage lh = this.world.getSubimage((int) this.t1.getScreen_x(), (int) this.t1.getScreen_y(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = this.world.getSubimage((int) this.t2.getScreen_x(), (int) this.t2.getScreen_y(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        onScreenPanel.drawImage(lh, 0, 0, null);
        onScreenPanel.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH / 2 + 2, 0, null);
    }

    public void addGameObject(GameObject g) {
        this.gObj.add(g);
    }
}
