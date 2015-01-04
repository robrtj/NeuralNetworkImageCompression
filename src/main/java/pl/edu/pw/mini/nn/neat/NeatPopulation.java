package pl.edu.pw.mini.nn.neat;

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
    private int maxIteration;
    private double maxError;

    //network params
    //TODO

    NeatPopulation(){
        Species = new LinkedList<>();
    }

    public NeatPopulation(int numberOfSpecies, int maxIteration, double maxError){
        this();

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

    public void generateFirstPopulation(int inputLayerSize, int middleLayerSize){
        Species = new LinkedList<>();
        for (int i = 0; i < numberOfSpecies; i++) {
            NeuralNetwork network = new NeuralNetwork(inputLayerSize, middleLayerSize);
            Species.add(network);
        }
    }

    public double[][] computeImage(double[][] image, int inputLayerSize, int middleLayerSize){
        setImage(image);
        generateFirstPopulation(inputLayerSize, middleLayerSize);

        for (int i = 0; i < maxIteration; i++) {
            iteration();
            if(getBestFitness() < maxError){
                break;
            }
        }

        return getBestCompressedImage();
    }

    //TODO
    private double[][] getBestCompressedImage() {
        return new double[0][];
    }

    private double iteration(){
        mutation();
        crossover();

        generateNextPopulation();

        return getBestFitness();
    }

    //TODO
    private void generateNextPopulation() {
        //count fitness
        //choose n-best nets
    }

    private double getBestFitness() {
        double bestFitness = Double.POSITIVE_INFINITY;
        for (NeuralNetwork net : Species){
            double fitness = net.fitnessFunction(image);
            bestFitness = bestFitness < fitness ? bestFitness : fitness;
        }
        return bestFitness;
    }

    //TODO
    private void crossover() {
    }

    private void mutation() {
        for (NeuralNetwork net : Species){
            net.mutation();
        }
    }

    public int size() {
        return Species.size();
    }
}
