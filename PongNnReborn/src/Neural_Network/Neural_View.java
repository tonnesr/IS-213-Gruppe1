/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Neural_Network;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

import pongnnreborn.Pong;
import pongnnreborn.Ball;

/**
 * This class creates a view of the neural network that shows a representation of how it works
 * It is made to lock like a visualisation of how a neural network works.
 * It will show the inputs it receives and the calculated output
 * 
 * @author Group 1
 */
public class Neural_View implements ActionListener {
     
    static final int viewWidth = 880;
    static final int viewHeight = 610;
    
    static double viewInput_1;
    static double viewInput_2;
    static double viewDesiredOutput;
    static double viewOutput;
    static int viewGeneration;
    
    static Neural_View view;
    static Neural_View_Rendering renderingView;
    static Neural_Network neuralNetwork;
    static Pong pong;
    static Ball ball;
    
    JFrame viewFrame;
    Timer viewTimer;
    
    public Neural_View() {
        renderingView = new Neural_View_Rendering();
        neuralNetwork = new Neural_Network(pong, ball);
        
        viewTimer = new Timer(2, this);
        viewFrame = new JFrame("Neural Network View");
        
        viewFrame.setSize(viewWidth, viewHeight);
        viewFrame.add(renderingView);
        viewFrame.setResizable(false);
        
        viewFrame.setVisible(true);  
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        viewTimer.start();
    }
    
    /**
     * Specifies what objects and shapes that are going to be rendered on the neural network view frame.
     * 
     * @param g AWT Graphics2D
     */
    protected static void render(Graphics2D g) {        
        //Get data to display.
        getLiveData();
        
        //Rendering stuff
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1));
                
        //Background
        g.setColor(Color.white);
        g.fillRect(0, 0, viewWidth, viewHeight);
        
        //Rendering shapes
        renderShapes(g);
        
        //Title
        g.setColor(Color.black);
        g.setFont(new Font("Arial", 1, 30));
        g.drawString("Neural Network Live View", 5, 26);
        
        g.setFont(new Font("Arial", 1, 20));
        //Input node 1 text
        g.drawString("Input 1", 54, 220);
        g.drawString(""+ viewInput_1, 72, 272);
        
        //Input node 2 text
        g.drawString("Input 2", 54, 372);
        g.drawString("" + viewInput_2, 72, 424);
        
        //Hidden layer text
        g.drawString("Hidden layer", 233, 95);
        //<----->Dispaly values of hidden nodes -???
        
        //Output text
        g.drawString("Output", 635, 292);
        g.drawString("" + viewOutput, 635, 340);
        
        //Activation Function text
        g.drawString("Activation", 465, 272);
        g.drawString("Function", 465, 292);
        g.drawString("Sigmoid", 480, 340);
        
        //Generation text
        g.drawString("Generation: " + viewGeneration, 5, 70);
        
        //Desired Output text
        g.drawString("Desired Output: " + viewDesiredOutput, 635, 400);
        
        //Information text
        g.drawString("BOT: ", 723, 20);
        g.drawString("NN: ", 738, 40);
        g.drawString("Live View: ", 677, 60);
        g.drawString("Load from File: ", 630, 80);
        
        //Information text true/false
        g.setColor(Color.red);       
        //BOT (1)
        if (Pong.botEnabled) {
            g.drawString("true", 780, 20);
        } else {
            g.drawString("false", 780, 20);
        }
        //Neural Network (2) 
        if (Pong.neuralNetworkEnabled) {
            g.drawString("true", 780, 40);
        } else {
            g.drawString("false", 780, 40);
        }        
        //Live View (3)
        if (Pong.liveViewEnabled) {
            g.drawString("true", 780, 60);
        } else {
            g.drawString("false", 780, 60);
        }        
        //From file (4)
        if (Neural_Network.fromFileEnabled) {
            g.drawString("true", 780, 80);
        } else {
            g.drawString("false", 780, 80);
        }  
        
//        g.drawString("Desired Output: " + viewDesiredOutput, 105, 60);
//        g.drawString("Output: " + viewOutput, 105, 110);     
        
    
    }
    
    /**
     * Helper to the render method, for easier understanding of content.
     * 
     * @param g AWT Graphics2D
     */
    private static void renderShapes(Graphics2D g) {
        g.setColor(Color.getHSBColor(16, 69, 100));
        
        //Input Node 1
        g.fillOval(50, 230, 75, 75);
        //Node 2
        g.fillOval(50, 380, 75, 75);
        
        //Hidden layer Node 1
        g.fillOval(250, 105, 75, 75);
        //Node 2
        g.fillOval(250, 200, 75, 75);
        //Node 3
        g.fillOval(250, 295, 75, 75);
        //Node 4
        g.fillOval(250, 390, 75, 75);
        //Node 5
        g.fillOval(250, 485, 75, 75);
          
        //Output node
        g.fillRect(630, 300, 220, 75);
        
        //SIGMOID Box
        g.setColor(Color.orange);
        g.fillRect(460, 300, 120, 75);        
        
        g.setColor(Color.black);
        
        //Input 1 -> Hidden Node 1
        g.drawLine(124, 269, 250, 145);
        //Node 2
        g.drawLine(124, 269, 250, 240);
        //Node 3
        g.drawLine(124, 269, 250, 335);
        //Node 4
        g.drawLine(124, 269, 250, 430);
        //Node 5
        g.drawLine(124, 269, 250, 525);
        
        //Input 2 -> Hidden Node 1 
        g.drawLine(124, 423, 250, 145);
        //Node 2
        g.drawLine(124, 423, 250, 240);
        //Node 3
        g.drawLine(124, 423, 250, 335);
        //Node 5
        g.drawLine(124, 423, 250, 430);
        //Node 5
        g.drawLine(124, 423, 250, 525);
        
        //Hidden Nodes -> Sigmoid
        //Node 1
        g.drawLine(324, 145, 460, 335);
        //Node 2
        g.drawLine(324, 240, 460, 335);
        //Node 3
        g.drawLine(324, 335, 460, 335);
        //Node 4
        g.drawLine(324, 430, 460, 335);
        //Node 5
        g.drawLine(324, 525, 460, 335);
        
        //Sigmoid -> Output
        g.drawLine(580, 335, 629, 335);
    }


    /**
     * Fetches the data from other classes.
     * 
     * viewGeneration = generation from Neural_Network
     * viewInput_1 = Input 1 from Neural_Network
     * viewInput_2 = Input 2 from Neural_Network
     * viewDesiredOutput = The desired output from Neural_Network
     * viewOutput = The calculated output from Neural_Network
     */
    private static void getLiveData() {
        viewGeneration = neuralNetwork.getGeneration();
        viewInput_1 = neuralNetwork.getInput_1();
        viewInput_2 = neuralNetwork.getInput_2();
        viewDesiredOutput = neuralNetwork.getdesOutput();
        viewOutput = neuralNetwork.getNeuralNetwork_Output();
    }
    
    /**
     * Listens for the timer which actives every 2 milliseconds. 
     * 
     * @param e ActionEvent from AWT
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        renderingView.repaint();
    }
}
