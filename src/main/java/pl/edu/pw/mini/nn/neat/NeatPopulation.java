package pl.edu.pw.mini.nn.neat;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Pawel on 2015-01-03.
 */
public class NeatPopulation {
    private List<NeuralNetwork> Species;
    private double[][] image;

    //neat params
    private int numberOfSpecies;
    private int actualIteration;
    private int maxIteration;
    private int maxError;

    //network params
    //TODO

    NeatPopulation(){
        actualIteration = 0;
    }

    //TODO
    public NeatPopulation(int numberOfSpecies, int maxIteration, int maxError){
        this();

        //TODO
        //fill net parameters
        this.numberOfSpecies = numberOfSpecies;
        this.maxIteration = maxIteration;
        this.maxError = maxError;
    }

    public List<NeuralNetwork> getSpecies() {
        return Species;
    }

    public void setSpecies(List<NeuralNetwork> species) {
        Species = species;
    }

    public void setImage(double[][] image) {
        this.image = image;
    }

    public void generateEmptyPopulation(int inputLayerSize, int middleLayerSize){
        List<NeuralNetwork> firstPopulation = new LinkedList<NeuralNetwork>();
        for (int i = 0; i < numberOfSpecies; i++) {
            NeuralNetwork network = new NeuralNetwork(inputLayerSize, middleLayerSize);
            Species.add(network);
        }
    }

    //TODO
    //return: generate best compressed image in vector format
    public double[][] computeImage(double[][] image, int inputLayerSize, int middleLayerSize){
        setImage(image);
        generateEmptyPopulation(inputLayerSize, middleLayerSize);

        for (int i = 0; i < maxIteration; i++) {
            iteration();
            if(getBestFitness() < maxError){
                break;
            }
        }

        return null;
    }

    private double iteration(){
        mutation();
        crossover();
        updateFitness();

        ++actualIteration;
        return getBestFitness();
    }

    private double getBestFitness() {
        int minIndex = Species.indexOf(Collections.min(Species));
        return Species.get(minIndex).getFitness();
    }

    private void updateFitness() {
        for (NeuralNetwork net : Species){
            net.updateFitness();
        }
    }

    //TODO
    private void crossover() {
    }

    private void mutation() {
        for (NeuralNetwork net : Species){
            net.mutation();
        }
    }
}
