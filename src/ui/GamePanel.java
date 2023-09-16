package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    //declaring everything needed for the program
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //how bug the objects going to be in the game
    static final int UNIT_SIZE = 25;
    //how many objects you can fit on the screen
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    //delay for timer (higher number = slower the game is)
    static final int DELAY = 100;
    // creating two arrays for snake body parts
    // snake will never be bigger than GAME_UNITS
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //intial number of body parts
    int bodyParts = 6;
    //initially will be 0
    int applesEaten;
    //will appear randomly
    int appleX;
    int appleY;

    //snake will begin going right at the start
    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;



    //Constructor
    public GamePanel(){
        random = new Random();
        Dimension dimension = new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setPreferredSize(dimension);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKeyAdapter());
        startGame();
    }

    public void startGame(){
        //so it can create a new apple on the screen
        newApple();
        running = true;

        //this will dictate how fast the game is running
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.gray);
            //making a grid
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //to draw the snake we iterate through the body parts.
            for (int i = 0; i < bodyParts; i++) {
                //head of snake
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));

            // we will use font metrics to line text in the middle
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - fm.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());


        }
        else{
            gameOver(g);
        }


    }

    // generating new apple coordinates
    public void newApple(){
        // apple will appear somewhere along the x axis
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    //will move the snake
    public void move(){

        //for loop to iterate through all the body parts of the snake
        for(int i=bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        //changes the direction
        switch (direction){
            //up
            case 'U':
                //y[0] is head of the snake
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

        }

    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    //if the head collides with body
    public void checkCollisions(){

        //checks if head collides with bodu
        for (int i = bodyParts; i>0; i--){
            //head collides with body
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running =false;
        }
        //checks if head touches left border
        if(x[0] < 0){
            running =false;
        }
        //checks if head touches top border
        if(y[0] < 0){
            running =false;
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running =false;
        }

        if (running == false){
            timer.stop();
        }


    }

    public void gameOver(Graphics g){

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        // we will use font metrics to line text in the middle
        FontMetrics fmScore = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - fmScore.stringWidth("Score:" + applesEaten))/2, g.getFont().getSize());

        //text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));

        // we will use font metrics to line text in the middle
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("GAME OVER" , (SCREEN_WIDTH - fm.stringWidth("GAME OVER" ))/2, SCREEN_HEIGHT/2 );


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    //creating an inner class
    public class SnakeKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }

}
