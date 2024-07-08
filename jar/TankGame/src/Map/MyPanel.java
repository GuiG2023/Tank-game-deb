package Map;

import javax.swing.*;
import java.awt.*;

/**
 * 7/6/24 @ 20:34
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class MyPanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //g.setColor(Color.black);
        //g.fillOval(10,10,200,200);
        //g.drawOval(10,10,200,200);
        Image image = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/resources/tank1.png"));
        g.drawImage(image,200,200,100,90,this);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Calibri",Font.BOLD,30));
        g.drawString("Player 1",180,180);
    }

}
