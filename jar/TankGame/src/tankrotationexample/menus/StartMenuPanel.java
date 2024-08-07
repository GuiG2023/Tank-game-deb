package tankrotationexample.menus;


import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;

    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        this.menuBackground = ResourceManager.getSprites("menu");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 150, 50);
        start.addActionListener(actionEvent -> this.lf.setFrame("game"));

        JButton exit = new JButton("Exit");
        exit.setSize(new Dimension(200, 100));
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 375, 150, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        JButton help = new JButton("Help");
        help.setFont(new Font("Courier New", Font.BOLD, 24));
        help.setBounds(150, 450, 150, 50);
        help.addActionListener(actionEvent -> displayHelpDialog());

        this.add(start);
        this.add(exit);
        this.add(help);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }

    private void displayHelpDialog() {// with chatgpt's help to finsh this function
        JDialog helpDialog = new JDialog();
        helpDialog.setTitle("Game Introduction & Rules");

        JTextArea textArea = new JTextArea( "Player Tank Control:\n" +
                "P1: Use 'WASD' keys to move the tank, and 'Space' to shoot.\n" +
                "P2: Use arrow keys (Up/Down/Left/Right) to move the tank, and 'P' to shoot.\n\n" +
                "Game Terrain Introduction:\n" +
                "- Impassable Concrete Walls: These are located at the map's edges and cannot be breached.\n" +
                "- Indestructible Walls: These require ten continuous hits for destruction.\n" +
                "- Destructible Walls: Can be destroyed by one hit or by ramming, though ramming causes a brief halt to your tank.\n" +
                "- Sand: Tanks slow down when entering sand and do not immediately regain speed upon exiting. Speed can be increased by picking up a speed power-up or entering rivers for cleaning.\n" +
                "- Rivers: Flowing river water will carry the tank slowly in the direction of the flow, even if no movement keys are pressed.\n" +
                "- Power-Ups: \n" +
                "  - Shooting Boost: Enables shooting in multiple directions simultaneously for 30 seconds.\n" +
                "  - Speed Boost: Increases tank speed to its maximum.\n" +
                "  - Extra Life: Grants an additional life.\n\n" +
                "Enemy Bunkers:\n" +
                "- Multiple enemy bunkers are generated at random locations at the start of each game. They are stationary but will rotate towards the player's tank and attack, and they can be destroyed.\n\n" +
                "Enjoy the game!\n"+"Best\n"+"Guiran");
        textArea.setFont(new Font("Courier New", Font.BOLD, 16));
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(30);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        helpDialog.add(scrollPane);

        helpDialog.setSize(400, 300);
        helpDialog.setLocationRelativeTo(null);
        helpDialog.setVisible(true);

    }
}
