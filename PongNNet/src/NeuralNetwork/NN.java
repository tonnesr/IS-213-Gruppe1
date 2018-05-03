/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNetwork;

import java.io.IOException;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TrainingSetImport;
import org.neuroph.util.TransferFunctionType;
import pongnnet.PongNNet;

/**
 *
 * @author vegar
 */
public class NN{   
    private NeuralNetwork neuralNetwork;
    private MomentumBackpropagation learningRule;
    private DataSet trainingSet;
    
    private final int inputCount;
    private final int outputCount;
    private final int hiddenLayers;
    private final String fileName;   
    private int generation;
    private PongNNet pong;

    public NN(PongNNet pong){
        inputCount = 3; //Position pad, ball (x,y) score
        outputCount = 1;
        hiddenLayers = 20;
        fileName = "data.txt";
        neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputCount, hiddenLayers, outputCount);
        learningRule = (MomentumBackpropagation) neuralNetwork.getLearningRule();
        learningRule.setLearningRate(0.5); //vet ikke om disse verdiene er korrekte    
        learningRule.setMomentum(0.8);
        generation = 0;
        this.pong = pong;
    }
    
    /**
     * Mulig løsning; skrive data fra et spill til en fil, NN lærer datasettet før neste spill
     */
    public void trainNN(){
        System.out.println("Learning");
        System.out.print(trainingSet);
        
//        System.out.println("EMUALTE TRAINING");
        neuralNetwork.learn(trainingSet);        
        System.out.println("Successfully learned data.");
        
        test();
    }
    
    public void trainingSet() {
        System.out.println("Loading training data from: " + fileName);
        try{
            trainingSet = TrainingSetImport.importFromFile(fileName, inputCount, outputCount, ",");
        }
        catch (IOException | NumberFormatException e) {
            System.out.print("Error: ");
            System.out.println(e);
        }
        System.out.println("Successfully loaded data.");
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void test() {
        for(DataSetRow dataRow : trainingSet.getRows()) {
            neuralNetwork.setInput(dataRow.getInput());
            neuralNetwork.calculate();
            double[ ] networkOutput = neuralNetwork.getOutput();
            System.out.println("Input: " + Arrays.toString(dataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) );
            playPong();
        }
    }
       
    /**
     *
     * @return 
     */
    public void playPong(){
        double[] output = neuralNetwork.getOutput();
        double o = output[0];
        if (o<0.9){
            pong.nnRelease('s');
        pong.nnTypes('w');
        }
        else{
            pong.nnRelease('w');
            pong.nnTypes('s');
        }
        
    }
    
}
