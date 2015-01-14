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
            Species.add(new FitnessNetworkWrapper(-Double.POSITIVE_INFINITY, network));
        }
    }

    public double[][] computeImage(double[][] image, int inputLayerSize, int middleLayerSize) {
        setImage(image);
        generateFirstPopulation(inputLayerSize, middleLayerSize);

        bestNet = new FitnessNetworkWrapper(-Double.POSITIVE_INFINITY, null);
        for (int i = 0; i < maxIteration; i++) {
            System.out.println("iteration " + (i + 1));
            iteration();
            bestNet = getBestFitness();
            System.out.println("Error: " + bestNet.fitness);
            if (Math.abs(bestNet.fitness) < maxError) {
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
        crossover();
        generateNextPopulation();
    }

    private void generateNextPopulation() {
        computeFitness();
        takeNBestSpecies();

//        double fitnessSum = 0;
//        for (FitnessNetworkWrapper individual : Species) {
//            fitnessSum += individual.fitness;
//        }
//        for (FitnessNetworkWrapper individual : Species) {
////            individual.fitness = (fitnessSum - individual.fitness) / fitnessSum;
//            individual.fitness /= fitnessSum;
//        }
//        Collections.sort(Species);
//        Collections.reverse(Species);
//
//        List<FitnessNetworkWrapper> generation = new LinkedList<>();
//        for (int i = 0; i < numberOfSpecies; i++) {
////            generation.add(Species.get(i));
//            double threshold = randomGenerator.nextDouble();
//            double sum = 0;
//            for (FitnessNetworkWrapper individual : Species) {
//                sum += individual.fitness;
//                if (sum > threshold) {
//                    generation.add(individual.clone());
//                    break;
//                }
//            }
//        }
//        Species = new ArrayList<>();
//        Species.addAll(generation);
//
//        //return true fitness
//        for (FitnessNetworkWrapper individual : Species) {
////            individual.fitness = fitnessSum - fitnessSum * individual.fitness;
//            individual.fitness *= fitnessSum;
//        }
    }

    private void takeNBestSpecies(){
        Collections.sort(Species);
        Collections.reverse(Species);

        List<FitnessNetworkWrapper> generation = new LinkedList<>();
        for (int i = 0; i < numberOfSpecies; i++) {
            FitnessNetworkWrapper individual = Species.get(i);
            generation.add(individual.clone());
        }

        Species = new ArrayList<>();
        Species.addAll(generation);
    }

    private FitnessNetworkWrapper getBestFitness() {
        FitnessNetworkWrapper bestNet = new FitnessNetworkWrapper(-Double.POSITIVE_INFINITY, null);
        for (FitnessNetworkWrapper individual : Species) {
            double fitness = individual.fitness;
            if (bestNet.fitness < fitness) {
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
                FitnessNetworkWrapper wrapper = new FitnessNetworkWrapper(-Double.POSITIVE_INFINITY, child);
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


    class FitnessNetworkWrapper implements Cloneable, Comparable<FitnessNetworkWrapper> {
        double fitness;
        NeuralNetwork network;

        public FitnessNetworkWrapper(double fitness, NeuralNetwork network) {
            this.fitness = fitness;
            this.network = network;
        }

        @Override
        public int compareTo(FitnessNetworkWrapper o) {
            double a = fitness - o.fitness;
            return a > 0 ? 1 : a == 0 ? 0 : -1;
        }

        @Override
        public FitnessNetworkWrapper clone(){
            return new FitnessNetworkWrapper(fitness, network.clone());
        }
    }
}
