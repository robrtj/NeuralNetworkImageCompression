package pl.edu.pw.mini.nn.neat;

import javafx.util.Pair;
import org.apache.commons.math3.geometry.partitioning.utilities.OrderedTuple;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Pawel on 2015-01-03.
 */
public class NeatPopulation {
    private List<NeuralNetwork> Species;
    private double[][] image;
    private MutationFactory mutationFactory;

    //neat params
    private int numberOfSpecies;
    private int maxIteration;
    private double maxError;
    //TODO
    //crossover ratio
    private double mutationRatio;

    //TODO
    //network params

    NeatPopulation() {
        Species = new LinkedList<>();
        mutationFactory = new MutationFactory();
    }

    public NeatPopulation(int numberOfSpecies, int maxIteration, double maxError, double mutationRatio) {
        this();

        this.numberOfSpecies = numberOfSpecies;
        this.maxIteration = maxIteration;
        this.maxError = maxError;
        this.mutationRatio = mutationRatio;
    }

    //TODO
    public void setNetworkParameters() {
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

    public void generateFirstPopulation(int inputLayerSize, int middleLayerSize) {
        Species = new LinkedList<>();
        for (int i = 0; i < numberOfSpecies; i++) {
            NeuralNetwork network = new NeuralNetwork(inputLayerSize, middleLayerSize);
            Species.add(network);
        }
    }

    public double[][] computeImage(double[][] image, int inputLayerSize, int middleLayerSize) {
        setImage(image);
        generateFirstPopulation(inputLayerSize, middleLayerSize);

        for (int i = 0; i < maxIteration; i++) {
            iteration();
            if (getBestFitness() < maxError) {
                break;
            }
        }

        return getOutputImage();
    }

    //TODO
    public double[][] getOutputImage() {
        return new double[0][0];
    }

    //TODO
    public double[][] getCompressedImage() {
        return new double[0][0];
    }

    private double iteration() {
        mutation();
        crossover();
        generateNextPopulation();

        return getBestFitness();
    }

    private void generateNextPopulation() {

        List<NeuralNetworkFitness> tuples = new LinkedList<>();
        for (int i = 0; i < size(); i++) {
            NeuralNetwork net = Species.get(i);
            double fitness = net.fitnessFunction(image);
            tuples.add(new NeuralNetworkFitness(fitness, net));
        }
        Collections.sort(tuples);

        Species = new LinkedList<>();
        for (int i = 0; i < size(); i++) {
            Species.add(tuples.get(i).net);
        }
    }

    class NeuralNetworkFitness implements Comparable<NeuralNetworkFitness> {
        double fitness;
        NeuralNetwork net;

        NeuralNetworkFitness(double fitness, NeuralNetwork net) {
            this.fitness = fitness;
            this.net = net;
        }

        @Override
        public int compareTo(NeuralNetworkFitness o) {
            return fitness < o.fitness ? -1 : 1;
        }
    }

    private double getBestFitness() {
        double bestFitness = Double.POSITIVE_INFINITY;
        for (NeuralNetwork net : Species) {
            double fitness = net.fitnessFunction(image);
            bestFitness = bestFitness < fitness ? bestFitness : fitness;
        }
        return bestFitness;
    }

    //TODO
    private void crossover() {
    }

    private void mutation() {
        Random rand = new Random();
        for (NeuralNetwork net : Species) {
            if (rand.nextDouble() < mutationRatio) {
                mutationFactory.mutate(net);
            }
        }
        //testy sa odpalane z flaga "-source 1.7"
        // wiec nie ma stream'a z java8,
        // a nie chce sie bawic z konfiguracja, bo gradle'a nie znam
        //Species.stream().filter(net -> rand.nextDouble() < mutationRatio).forEach(mutationFactory::mutate);
    }

    public int size() {
        return Species.size();
    }
}
