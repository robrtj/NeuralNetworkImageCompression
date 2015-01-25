package pl.edu.pw.mini.nn;

import pl.edu.pw.mini.nn.image.GrayImageParser;
import pl.edu.pw.mini.nn.neat.NeatPopulation;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationBiPolar;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationSigmoidBiPolar;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationSigmoidUniPolar;

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
        ActivationFunction function = new ActivationSigmoidBiPolar();

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

        boolean tests = false;
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
        String imagePath = "gray2.png";
        int inputLayerSize = 64;
        int middleLayerSize = 16;
        double maxError = 1;
        ActivationFunction function = new ActivationSigmoidBiPolar();
        GrayImageParser imageParser = new GrayImageParser(imagePath, inputLayerSize, false, function.getType());
        int counter = 25;
        int max = 100;

        long time = 0;
        long tStart = System.currentTimeMillis();
        for (int numberOfSpecies = 2 * counter; numberOfSpecies <= 2 * max; numberOfSpecies += counter) {
            for (int maxIteration = 4 * counter; maxIteration <= 10 * max; maxIteration += 2 * counter) {
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

                        //elapsed time for one test
                        long tEnd = System.currentTimeMillis();
                        long tDelta = tEnd - tStart;
                        int elapsedSeconds = (int) (tDelta / 1000);
                        time += tDelta;
                        long timeInSeconds = time / 1000;
                        System.out.println("Elapsed time: " + elapsedSeconds / 3600 + ":" + (elapsedSeconds / 60) % 60 + ":" + elapsedSeconds % 60);
                        System.out.println("All elapsed time: " + timeInSeconds / 3600 + ":" + (timeInSeconds / 60) % 60 + ":" + timeInSeconds % 60 + "\n");
                    }
                }
            }
        }
    }
}
