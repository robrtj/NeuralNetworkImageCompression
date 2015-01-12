package pl.edu.pw.mini.nn.neat;

import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.util.*;

/**
 * Created by Pawel on 2015-01-03.
 */
public class NeatPopulation {
    private List<FitnessNetworkWrapper> Species;
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
    private ActivationFunction activationFunction;

    private FitnessNetworkWrapper bestNet;

    NeatPopulation() {
        Species = new LinkedList<>();
        mutationFactory = new MutationFactory();
        crossoverFactory = new CrossoverFactory();
        activationFunction = new ActivationUniPolar();
        bestNet = null;
    }

    public NeatPopulation(int numberOfSpecies, int maxIteration,
                          double maxError, double mutationRatio, double crossoverRatio,
                          ActivationFunction function) {
        this();

        this.numberOfSpecies = numberOfSpecies;
        this.maxIteration = maxIteration;
        this.maxError = maxError;
        this.mutationRatio = mutationRatio;
        this.crossoverRatio = crossoverRatio;
        this.activationFunction = function.clone();
    }

    public void setImage(double[][] image) {
        this.image = image;
    }

    private void generateFirstPopulation(int inputLayerSize, int middleLayerSize) {
        Species = new LinkedList<>();
        for (int i = 0; i < numberOfSpecies; i++) {
            NeuralNetwork network = new NeuralNetwork(inputLayerSize, middleLayerSize, activationFunction);
            Species.add(new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, network));
        }
    }

    public double[][] computeImage(double[][] image, int inputLayerSize, int middleLayerSize) {
        setImage(image);
        generateFirstPopulation(inputLayerSize, middleLayerSize);

        bestNet = new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, null);
        for (int i = 0; i < maxIteration; i++) {
            System.out.println("iteration " + (i + 1));
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

        return net.getNetworkOutput(image);
    }

    public double[][] getCompressedVector(double[][] image) {
        FitnessNetworkWrapper bestNet = getBestFitness();
        return bestNet.network.getCompressedVector(image);
    }

    private void iteration() {
        mutation();
//        crossover();
        generateNextPopulation();
    }

    private void generateNextPopulation() {
        computeFitness();

        double fitnessSum = 0;
        for (FitnessNetworkWrapper individual : Species) {
            fitnessSum += individual.fitness;
        }
        for (FitnessNetworkWrapper individual : Species) {
            individual.fitness /= fitnessSum;
        }
        Collections.sort(Species);

        List<FitnessNetworkWrapper> generation = new LinkedList<>();
        for (int i = 0; i < numberOfSpecies; i++) {
            double threshold = randomGenerator.nextDouble();
            double sum = 0;
            for (FitnessNetworkWrapper individual : Species) {
                sum += individual.fitness;
                if (sum < threshold) {
                    generation.add(individual);
                    break;
                }
            }
        }
        Species = new ArrayList<>();
        Species.addAll(generation);

        //return true fitness
        for (FitnessNetworkWrapper individual : Species) {
            individual.fitness *= fitnessSum;
        }
    }

    private FitnessNetworkWrapper getBestFitness() {
        FitnessNetworkWrapper bestNet = new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, null);
        for (FitnessNetworkWrapper individual : Species) {
            double fitness = individual.fitness;
            if (bestNet.fitness > fitness) {
                bestNet = individual;
            }
        }
        return bestNet;
    }

    private void computeFitness() {
        for (FitnessNetworkWrapper individual : Species) {
            individual.fitness = individual.network.fitnessFunction(image);
        }
    }

    private void crossover() {
        List<FitnessNetworkWrapper> children = new LinkedList<>();
        for (FitnessNetworkWrapper individual : Species) {
            NeuralNetwork firstParent = individual.network;
            if (randomGenerator.nextDouble() < crossoverRatio) {
                NeuralNetwork secondParent = Species.get(randomGenerator.nextInt(Species.size())).network;
                NeuralNetwork child = crossoverFactory.cross(firstParent, secondParent);
                FitnessNetworkWrapper wrapper = new FitnessNetworkWrapper(Double.POSITIVE_INFINITY, child);
                children.add(wrapper);
            }
        }
        Species.addAll(children);
    }

    private void mutation() {
        for (FitnessNetworkWrapper individual : Species) {
            NeuralNetwork net = individual.network;
            if (randomGenerator.nextDouble() < mutationRatio) {
                mutationFactory.mutate(net);
            }
        }
    }

    public int size() {
        return Species.size();
    }


    class FitnessNetworkWrapper implements Comparable<FitnessNetworkWrapper> {
        double fitness;
        NeuralNetwork network;

        FitnessNetworkWrapper(double fitness, NeuralNetwork network) {
            this.fitness = fitness;
            this.network = network;
        }

        @Override
        public int compareTo(FitnessNetworkWrapper o) {
            return fitness < o.fitness ? -1 : 1;
        }
    }
}
