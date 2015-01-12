package pl.edu.pw.mini.nn;

import pl.edu.pw.mini.nn.image.GrayImageParser;
import pl.edu.pw.mini.nn.neat.NeatPopulation;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationBiPolar;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;

/**
 * Created by Robert on 2015-01-02.
 */
public class MainClass {
    public static void main(final String args[]) {
        String imagePath = "lena_1.png";
        int inputLayerSize = 64;
        int middleLayerSize = 40;
        int numberOfSpecies = 10;
        int maxIteration = 1000;
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

        NeatPopulation population = new NeatPopulation(numberOfSpecies, maxIteration, maxError, mutationRatio,
                crossoverRatio, function);
        GrayImageParser imageParser = new GrayImageParser(imagePath, inputLayerSize, false, function.getType());
        double[][] output = population.computeImage(imageParser.getNetworkInput(), inputLayerSize, middleLayerSize);
        imageParser.saveNetworkOutputAsImage(output, "out.png");
    }
}
