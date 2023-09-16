package ui;

import javax.swing.*;

public class GameFrame extends JFrame {
    //Constructor
    public GameFrame(){

        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //if you add components to JFrame this fucntion will take Jfrme
        // and fit all the components to the frame
        this.pack();
        this.setVisible(true);
        // will appear in the middle of your computer.
        this.setLocationRelativeTo(null);
    }
}
