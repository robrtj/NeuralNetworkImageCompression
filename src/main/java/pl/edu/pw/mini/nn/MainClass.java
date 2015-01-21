package pl.edu.pw.mini.nn;

import pl.edu.pw.mini.nn.image.GrayImageParser;
import pl.edu.pw.mini.nn.neat.NeatPopulation;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationBiPolar;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationSigmoidBiPolar;

/**
 * Created by Robert on 2015-01-02.
 */
public class MainClass {
    public static void main(final String args[]) {
        String imagePath = "gray2.png";
        int inputLayerSize = 64;
        int middleLayerSize = 40;
        int numberOfSpecies = 50;
        int maxIteration = 500;
        double maxError = 0.01;
        double mutationRatio = 0.9;
        double crossoverRatio = 0.9;
        ActivationFunction function = new ActivationBiPolar();

        try {
            imagePath = args[0];
            inputLayerSize = Integer.parseInt(args[1]);
            middleLayerSize = Integer.parseInt(args[2]);
            numberOfSpecies = Integer.parseInt(args[3]);
            maxIteration = Integer.parseInt(args[4]);
            maxError = Double.parseDouble(args[5]);
            mutationRatio = Double.parseDouble(args[6]);
            crossoverRatio = Double.parseDouble(args[7]);
        } catch (IndexOutOfBoundsException ex) {
        }

        boolean tests = true;
        if (tests) {
            runParamTests();
        } else {
            NeatPopulation population = new NeatPopulation(numberOfSpecies, maxIteration, maxError, mutationRatio,
                    crossoverRatio, function);

            GrayImageParser imageParser = new GrayImageParser(imagePath, inputLayerSize, false, function.getType());
            population.setImageParser(imageParser);
            double[][] output = population.computeImage(imageParser.getNetworkInput(), inputLayerSize, middleLayerSize);

            population.saveErrorToFile();
            imageParser.saveNetworkOutputAsImage(output, "out.png");
        }
    }

    private static void runParamTests() {
        String imagePath = "lena_1.png";
        int inputLayerSize = 64;
        int middleLayerSize = 32;
        double maxError = 1;
        ActivationFunction function = new ActivationSigmoidBiPolar();

        GrayImageParser imageParser = new GrayImageParser(imagePath, inputLayerSize, false, function.getType());

        int counter = 25;
        int max = 150;
        for (int numberOfSpecies = 25; numberOfSpecies <= max; numberOfSpecies += counter) {
            for (int maxIteration = 25; maxIteration <= 2*max; maxIteration += counter) {
                for (double mutationRatio = 0.1; mutationRatio <= 1; mutationRatio += 0.25) {
                    for (double crossoverRatio = 0.1; crossoverRatio <= 1; crossoverRatio += 0.25) {
                        System.out.println("species:" + numberOfSpecies);
                        System.out.println("iterations:" + maxIteration);
                        System.out.println("mutationRatio:" + mutationRatio);
                        System.out.println("crossoverRatio:" + crossoverRatio);
                        NeatPopulation population = new NeatPopulation(numberOfSpecies, maxIteration, maxError, mutationRatio,
                                crossoverRatio, function);
                        population.setImageParser(imageParser);
                        double[][] output = population.computeImage(imageParser.getNetworkInput(), inputLayerSize, middleLayerSize);
                        imageParser.saveNetworkOutputAsImage(output, "images\\out_" + numberOfSpecies + "_" + maxIteration + "_" + mutationRatio + "_" + crossoverRatio + ".png");
                    }
                }
            }
        }

        NeatPopulation population = new NeatPopulation(50, 300, maxError, 0.9, 0.1, function);
        population.setImageParser(imageParser);
        double[][] output = population.computeImage(imageParser.getNetworkInput(), inputLayerSize, middleLayerSize);
        imageParser.saveNetworkOutputAsImage(output, "images\\out_BIG1" + 50 + "_" + 1000 + "_" + 0.8 + "_" + 0.2 + ".png");

        population = new NeatPopulation(50, 1000, maxError, 0.8, 0.2, function);
        population.setImageParser(imageParser);
        output = population.computeImage(imageParser.getNetworkInput(), inputLayerSize, middleLayerSize);
        imageParser.saveNetworkOutputAsImage(output, "images\\out_BIG2" + 50 + "_" + 1000 + "_" + 0.8 + "_" + 0.2 + ".png");
    }
}
