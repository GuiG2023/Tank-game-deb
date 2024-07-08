package Map;

import javax.swing.*;
import java.awt.*;

/**
 * 7/6/24 @ 20:28
 *
 * @ Author : Guiran LIU
 * Description:
 */
public class DrawCircle  extends JFrame {
    private MyPanel mp = null;

    public static void main(String[] args) {
      new DrawCircle();
    }
    public DrawCircle(){
        mp = new MyPanel();
        this.add(mp);
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }
}
