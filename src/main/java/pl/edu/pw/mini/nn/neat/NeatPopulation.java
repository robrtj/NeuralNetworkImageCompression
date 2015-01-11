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
    private CrossoverFactory crossoverFactory;
    private Random randomGenerator = new Random();

    //neat params
    private int numberOfSpecies;
    private int maxIteration;
    private double maxError;

    private double mutationRatio;
    private double crossoverRatio;

    private FitnessNetworkWrapper bestNet;

    NeatPopulation() {
        Species = new LinkedList<>();
        mutationFactory = new MutationFactory();
        crossoverFactory = new CrossoverFactory();
        bestNet = null;
    }

    public NeatPopulation(int numberOfSpecies, int maxIteration, double maxError, double mutationRatio, double crossoverRatio) {
        this();

        this.numberOfSpecies = numberOfSpecies;
        this.maxIteration = maxIteration;
        this.maxError = maxError;
        this.mutationRatio = mutationRatio;
        this.crossoverRatio = crossoverRatio;
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

        bestNet = new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, null);
        for (int i = 0; i < maxIteration; i++) {
            iteration();
            bestNet = getBestFitness();
            if (bestNet.fitness < maxError) {
                break;
            }
        }
        return getOutputImage(image, bestNet.network);
    }

    private double[][] getOutputImage(double[][] image, NeuralNetwork net) {
        if (net == null) {
            double[][] output = new double[1024][1024];
            for (int i = 0; i < 1024; i++) {
                for (int j = 0; j < 1024; j++) {
                    output[i][j] = 1.0;
                }
            }
            return output;
        }
        return net.getOutputImage(image);
    }

    private NeuralNetwork getBestNetwork() {
        FitnessNetworkWrapper bestNet = getBestFitness();
        return bestNet.network;
    }

    private void iteration() {
        mutation();
        crossover();
        generateNextPopulation();
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

    private FitnessNetworkWrapper getBestFitness() {
        FitnessNetworkWrapper bestNet = new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, null);
        for (NeuralNetwork net : Species) {
            double fitness = net.fitnessFunction(image);
            if (bestNet.fitness < fitness) {
                bestNet = new FitnessNetworkWrapper(fitness, net);
            }
        }
        return bestNet;
    }

    private void crossover() {
        List<NeuralNetwork> children = new LinkedList<>();
        for (NeuralNetwork firstParent : Species) {
            if (randomGenerator.nextDouble() < crossoverRatio) {
                NeuralNetwork secondParent = Species.get(randomGenerator.nextInt(Species.size()));
                NeuralNetwork child = crossoverFactory.cross(firstParent, secondParent);
                children.add(child);
            }
        }
        Species.addAll(children);
    }

    private void mutation() {
        for (NeuralNetwork net : Species) {
            if (randomGenerator.nextDouble() < mutationRatio) {
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

    class FitnessNetworkWrapper {
        private final double fitness;
        private final NeuralNetwork network;

        FitnessNetworkWrapper(double fitness, NeuralNetwork network) {
            this.fitness = fitness;
            this.network = network;
        }
    }
}
