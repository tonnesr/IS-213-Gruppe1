package pongnnreborn;

import Neural_Network.Neural_Network;
import Neural_Network.Neural_View;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @version 2.1.1
 * 
 * The main class for the pong game. 
 * 
 * @author Group 1
 */
public class Pong  implements ActionListener, KeyListener {
 
    int width = 700; //Frame width
    int height = 700; //Frame height
    public int gameScreen = 0; //0 - Start Menu, 1 - Playing the Game.
    int updateTimer = 15; //Timer for updating the frame (in milliseconds).
    
    int generation;
    
    public boolean w; //Player 1 up down
    public boolean s;
    public boolean up; //Player 2 up down
    public boolean down;
    
    public boolean zero; //Neural Network up
    public boolean one; //Neural Network down
    
    public static boolean botEnabled = true; //Enable / Disable bot
    public static boolean neuralNetworkEnabled = true; //Enable / Disable Neural Network
    public static boolean liveViewEnabled = true;
    
    static Pong pong;
    Rendering pongRendering;
    Paddle player1;
    Paddle player2;
    Ball ball;
    Neural_Network nn;
    Neural_View nn_view;
    
    JFrame pongFrame;
    Timer timer; 
    Random random;

    
    public Pong(){
        pongRendering = new Rendering();
        nn = new Neural_Network(this, ball);
        
        timer = new Timer(updateTimer, this);
        pongFrame = new JFrame("Pong");
        
        pongFrame.setResizable(false);
        pongFrame.setSize(width + 6, height + 29);
        pongFrame.setVisible(true);
        pongFrame.add(pongRendering);
        pongFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pongFrame.addKeyListener(this);
        
        timer.start();
    }
    
    /**
     * Main method, makes a new object of Pong and starts the game
     * 
     * @param args 
     */
    public static void main(String[] args) {
        pong = new Pong();
    }

    public void start() {
        gameScreen = 1;
        if (neuralNetworkEnabled && liveViewEnabled) {
            nn_view = new Neural_View();
        }
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);    
    }
    
    /**
     * Rendering the different shapes and strings for the pong frame.
     * 
     * @param g AWT Graphics2D
     */
    public void render(Graphics2D g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);   
        
        if (gameScreen == 0) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 150));           
            g.drawString("PONG", width / 2 - 215, 200);

            g.setFont(new Font("Arial", 1, 20));                           
            g.drawString("- Play", width / 2 -50, height / 2 - 25);
            
            if (botEnabled) {
                g.drawString("- BOT: ENABLED", width / 2 -50, height / 2 + 25);
            } else {
                g.drawString("- BOT: DISABLED", width / 2 -50, height / 2 + 25); 
            }
            
            if (neuralNetworkEnabled) {
                g.drawString("- Neural Network: ENABLED", width / 2 -50, height / 2 + 75);
            } else {
                g.drawString("- Neural Network: DISABLED", width / 2 -50, height / 2 + 75);
            }         
            
            if (liveViewEnabled && neuralNetworkEnabled) {
                g.drawString("- Live View: ENABLED", width / 2 -50, height / 2 + 125);
            } else {
                g.drawString("- Live View: DISABLED", width / 2 -50, height / 2 + 125); 
            }
            
            if (Neural_Network.fromFileEnabled) {
                g.drawString("- Loading from file: ENABLED", width / 2 -50, height / 2 + 175);
            } else {             
                g.drawString("- Loading from file: DISABLED", width / 2 -50, height / 2 + 175);
            }
            
            g.setColor(Color.red);
            g.drawString("(SPACE)", width / 2 -139, height / 2 - 25); 
            g.drawString("(1)", width / 2 -139, height / 2 + 25);
            g.drawString("(2)", width / 2 -139, height / 2 + 75);
            g.drawString("(3)",width / 2 -139, height / 2 + 125);
            g.drawString("(4)",width / 2 -139, height / 2 + 175);
                        

        }
        
        if (gameScreen == 1) {
            g.setColor(Color.black);
            
            g.setStroke(new BasicStroke(2));
            g.drawLine(width / 2, 0, width / 2, height);
            
            g.setFont(new Font("Arial", 1, 30));
            g.drawString(String.valueOf(player1.score), width / 2 -50, 50);
            g.drawString(String.valueOf(player2.score), width / 2 +39, 50);
            
            //Paddle 1
            if (neuralNetworkEnabled) {
                generation = nn.getGeneration();
                g.setColor(Color.blue);
                g.drawString("NN Generation " + generation, 10, 690);
            } else {
                g.setColor(Color.blue);
                g.drawString("HUMAN", 10, 690);                
            }
            
            //Paddle 2
            if (botEnabled) {   
                g.setColor(Color.red);
                g.drawString("BOT", width/2+230, 690);
            } else {
                g.setColor(Color.red);
                g.drawString("HUMAN", width/2+230, 690);
            }
            
            //Rendering
            player1.render(g);
            player2.render(g);
            ball.render(g);    
        }
    }

    /**
     * Updates everything every time the timer actives.
     * 
     */
    public void update() {
        if (!neuralNetworkEnabled) {
            if(w) {
                player1.move(true);
            }
            if(s) {
                player1.move(false);
            }
        } else {
            //Neural Network
            if(zero) {
                player1.move(true);
            }
            if (one) {
                player1.move(false);
            }
        }
        
        if (!botEnabled) {
            if(up) {
                player2.move(true);
            }
            if(down) {
                player2.move(false);
            }
        } else { 
            //BOT
            if (player2.paddleY + player2.paddleHeight / 2 < ball.ballY) {
                player2.move(false);                
            }
            else if (player2.paddleY + player2.paddleHeight / 2 > ball.ballY) {
                player2.move(true);              
            }
        }
        ball.update(player1, player2);
    }  
    
    /**
     * The way the neural network can play the pong game.
     * 
     * nnPress is pressing the button.
     * 
     * @param key the input the neural network came up with, based on inputs.
     */
    public void nnPress(char key){
        switch (key){
            case('w'): zero = true; break;
            case('s'): one = true; break;
        }
    }
    
    /**
     * nnRelease is releasing the button.
     * 
     * @param key the input the neural network came up with, based on inputs.
     */
    public void nnRelease(char key){
        switch (key){
            case('w'): zero = false; break;
            case('s'): one = false; break;
        }
    }    
    
    
    /**
     * Activates every time the timer activates, which is every 15 milliseconds.
     * 
     * @param e AWT ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameScreen == 1)  {
            update();
        }
        pongRendering.repaint();   
    }

    /**
     * Not in use.
     * 
     * @param e 
     */
    @Override
    public void keyTyped(KeyEvent e) {        
    }

    /**
     * When a button is pressed.
     * 
     * @param e A key or button id.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();
        switch (id) {
            case(KeyEvent.VK_W): w = true; break;           
            case(KeyEvent.VK_S): s = true; break;            
            case(KeyEvent.VK_UP): up = true; break;           
            case(KeyEvent.VK_DOWN): down = true; break;  
            
            case(KeyEvent.VK_ESCAPE): if (gameScreen == 1) { 
                gameScreen = 0;
            } else {
                pongFrame.dispatchEvent(new WindowEvent(pongFrame, WindowEvent.WINDOW_CLOSING)); //04.05.2018 Added
            } break; 
            
            case(KeyEvent.VK_1): if (gameScreen == 0) {  
                botEnabled = !botEnabled;
            } break;
            
            case(KeyEvent.VK_2): if (gameScreen == 0) {
                neuralNetworkEnabled = !neuralNetworkEnabled;               
            } break;  
            
            case(KeyEvent.VK_3): if (gameScreen == 0 && neuralNetworkEnabled) {
                liveViewEnabled = !liveViewEnabled;                  
            } break;
            
            case(KeyEvent.VK_4): if (gameScreen == 0) {                  
                Neural_Network.fromFileEnabled = !Neural_Network.fromFileEnabled;
            } break;
            
            case(KeyEvent.VK_SPACE): if (gameScreen == 0) { 
                start();
            } break;     
        }
    }

    /**
     * Key released
     * 
     * @param e 
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();
        
        switch (id) {
            case(KeyEvent.VK_W): w = false; break;
            case(KeyEvent.VK_S): s = false; break;
            case(KeyEvent.VK_UP): up = false; break;
            case(KeyEvent.VK_DOWN): down = false; break;
        }        
    }
}
