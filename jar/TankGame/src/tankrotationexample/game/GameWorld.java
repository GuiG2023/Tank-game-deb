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
    List<Animation> animations = new ArrayList<>();
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

        /**
         * Battle city classic start bgm
         */
        Sound startBgm = ResourceManager.getSound("start");
        startBgm.setVolume(1);
        startBgm.play();
        /**
         *
         * Street Fighter classic bgm: Guile's
         *
         * (But too loud to hear others', lol)
         */
//        try {
//            Thread.sleep(5000 + 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Sound theme = ResourceManager.getSound("theme");
//        theme.setVolume(1);
//        theme.loopContinue();
//        theme.play();

        try {
            this.animations.add(new Animation(100, 100, ResourceManager.getAnim("explosion_lg")));
            while (true) {
                this.tick++;
                for (int i = this.gObj.size() - 1; i >= 0; i--) {
                    if (this.gObj.get(i) instanceof Updatable u) {
                        u.update(this);
                    } else {
                        break;
                    }
                }
                //renderFrame();
                this.checkCollision();
                for (int i = 0; i < this.animations.size(); i++) {
                    this.animations.get(i).update();
                }

//                checkEnemyTankCount(); // check and create enemy if needed
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */

                //when to end the game?
                if (t1.isDestroyed() || t2.isDestroyed()) {
                    checkWinner();
                    this.lf.setFrame("end");
                    return;// Switch to end game panel// Exit the game loop
                }
                //if all enemies are destroyed

//                if (tick>500){ //end the game
//                    this.lf.setFrame("end");
//                    return;
//                }
                Thread.sleep(10);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkWinner() {
        if (t1.isDestroyed() && !t2.isDestroyed()) {
            displayWinner("Player 2 Wins!");
        } else if (!t1.isDestroyed() && t2.isDestroyed()) {
            displayWinner("Player 1 Wins!");
        }
    }

    private void displayWinner(String s) {
        JLabel winnerLabel = new JLabel(s, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 64));
        winnerLabel.setSize(this.getSize());
        winnerLabel.setForeground(Color.RED);

        this.add(winnerLabel);
        winnerLabel.setVisible(true);
        repaint();

        Sound startBgm = ResourceManager.getSound("end");
        startBgm.setVolume(1);
        startBgm.play();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       this.remove(winnerLabel);
    }

    private void checkCollision() {
        for (int i = 0; i < this.gObj.size(); i++) {
            GameObject object1 = this.gObj.get(i);
            for (int j = 0; j < this.gObj.size(); j++) {
                GameObject object2 = this.gObj.get(j);
                if (j == i) continue;
                if (object1.getHitbox().intersects(object2.getHitbox())) {
//                    System.out.println("Collision Detected");
                    handleCollision(object1, object2);
                }
            }
        }
        this.gObj.removeIf(GameObject::getHasCollided);
    }

    private void handleCollision(GameObject obj1, GameObject obj2) {
        if (obj2 instanceof Bullet && obj1 instanceof BreakableWall) {
            animations.add(new Animation((obj1.x), (obj1.y), ResourceManager.getAnim("explosion_sm")));

            ResourceManager.getSound("wallBroken").play();
            obj2.setHasCollided(true);
            obj1.setHasCollided(true);
        } else if (obj2 instanceof Bullet bullet && obj1 instanceof Tank tank) {
            if (bullet.getOwner() != tank.getTkID()) {
                animations.add(new Animation((obj1.x), (obj1.y), ResourceManager.getAnim("explosion_lg")));
                ResourceManager.getSound("tankBroken").play();
                obj2.setHasCollided(true);
                ((Tank) obj1).takeDamage();
                if (((Tank) obj1).isDestroyed()) {
                    obj1.setHasCollided(true);
                }
            }
        } else if (obj1 instanceof Tank && obj2 instanceof BreakableWall) {
            ((Tank) obj1).stopMovement();
            animations.add(new Animation((obj1.x), (obj1.y), ResourceManager.getAnim("explosion_sm")));
            obj2.setHasCollided(true);
        } else if (obj1 instanceof Tank && obj2 instanceof RiverWall) {
            ((Tank) obj1).carriedMovement();
            ((Tank) obj1).restoreMovement();
        } else if (obj1 instanceof Tank && obj2 instanceof Sand) {
            ((Tank) obj1).slowMovement();
        } else if (obj1 instanceof Tank && obj2 instanceof Speed) {
            ResourceManager.getSound("pickup1").play();
            obj2.setHasCollided(true);
            ((Tank) obj1).fastMovement();
        }else if (obj1 instanceof Tank && obj2 instanceof Health){
            ResourceManager.getSound("pickup2").play();
            ((Tank) obj1).addLife();
            obj2.setHasCollided(true);
        }else if (obj1 instanceof Tank && obj2 instanceof ShootEnhance){
            ResourceManager.getSound("pickup2").play();
            ((Tank) obj1).enableMultiDirectionalShooting();;
            obj2.setHasCollided(true);
        } else if (obj1 instanceof  Tank && obj2 instanceof Tank) {
            ((Tank) obj1).stopMovement();
            ((Tank) obj2).stopMovement();

        } else if (obj2 instanceof Bullet && obj1 instanceof UnbreakableWall2) {
            UnbreakableWall2 wall = (UnbreakableWall2) obj1;
            wall.increaseHitCount();
            obj2.setHasCollided(true);
            if (wall.isDestroyed()) {
                wall.setHasCollided(true);
            }
        } else if (obj1 instanceof Tank && obj2 instanceof UnbreakableWall2) {
            Tank tank = (Tank) obj1;
            UnbreakableWall2 wall = (UnbreakableWall2) obj2;
            Rectangle tankRect = tank.getHitBox();
            Rectangle wallRect = wall.getHitBox();
            if (tankRect.getMaxX() > wallRect.getMinX() && tank.getVx() > 0) {
                tank.setX((float) (wallRect.getMinX() - tankRect.width));
            } else if (tankRect.getMinX() < wallRect.getMaxX() && tank.getVx() < 0) {
                tank.setX((float) wallRect.getMaxX());
            }

            if (tankRect.getMaxY() > wallRect.getMinY() && tank.getVy() > 0) {
                tank.setY((float) (wallRect.getMinY() - tankRect.height));
            } else if (tankRect.getMinY() < wallRect.getMaxY() && tank.getVy() < 0) {
                tank.setY((float) wallRect.getMaxY());
            }

            tank.stopMovement2();
        }

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


        gObj.clear();
        enemyTanks.clear();
        for (int i = 0; i < 5; i++) {
            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
            enemyTanks.add(new Tank(x, y, 0, 0, 0, enemyImg));
        }
        InitializeGame();
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

        t1 = new Tank(200, 500, 0, 0, (short) 0, ResourceManager.getSprites("t1"));
        t2 = new Tank(1300, 1300, 0, 0, (short) 0, ResourceManager.getSprites("t2"));
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
        initializeEnemyTanks();
        // Initialize enemy tanks
//        for (int i = 0; i < MaxEnemies; i++) {
//            float x = (float) (Math.random() * GameConstants.GAME_SCREEN_WIDTH);
//            float y = (float) (Math.random() * GameConstants.GAME_SCREEN_HEIGHT);
//            Tank enemy = new EnemyTank(x, y, enemyImg, t1,t2);
//            enemyTanks.add(enemy);
//            this.addGameObject(enemy);
//        }

        //add obstacle
        // ubwall


    }
    public void initializeEnemyTanks() {
        Random random = new Random();
        for (int i = 0; i < MaxEnemies; i++) {
            float x, y;
            boolean clear;
            do {
                x = random.nextFloat() * GameConstants.GAME_SCREEN_WIDTH;
                y = random.nextFloat() * GameConstants.GAME_SCREEN_HEIGHT;
                Tank tempTank = new EnemyFort(x, y, enemyImg, t1, t2);
                clear = isPositionClear(tempTank.getHitBox());
            } while (!clear);

            Tank enemy = new EnemyFort(x, y, enemyImg, t1, t2);
            enemyTanks.add(enemy);
            this.addGameObject(enemy);
        }
    }

    private void renderFrame() {
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);

        for (int i = 0; i < this.gObj.size(); i++) {
            this.gObj.get(i).drawImage(buffer);
        }
        for (int i = 0; i < this.animations.size(); i++) {
            this.animations.get(i).render(buffer);
        }

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
        for (int i = 0; i < this.animations.size(); i++) {
            this.animations.get(i).render(buffer);
        }
        //buffer.drawImage(ResourceManager.getSprites("background"), 0, 0, this.getWidth(), this.getHeight(), this);
//        buffer.setColor(Color.black);
// dbuffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        List<GameObject> copy = new ArrayList<>(gObj);
        copy.forEach(go -> go.drawImage(buffer));
        if (!this.t1.getHasCollided()) {
            this.t1.drawImage(buffer);
        }
        if (!this.t2.getHasCollided()) {
            this.t2.drawImage(buffer);
        }

//        for (Tank enemyTank : enemyTanks) {
//            enemyTank.drawImage(buffer);
//        }
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
    private boolean isPositionClear(Rectangle newTankRect) {
        for (GameObject obj : gObj) {
            if (newTankRect.intersects(obj.getHitbox())) {
                return false;
            }
        }
        return true;
    }
}
