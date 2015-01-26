package pl.edu.pw.mini.nn.neat;

import org.junit.Test;
import pl.edu.pw.mini.nn.image.GrayImageParser;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2015-01-04.
 */
public class NeatPopulationTest {

    @Test
    public void generateEmptyPopulation() throws Exception {
        int species = 100;
        NeatPopulation neat = new NeatPopulation(species, 100, 0.001d, 0.5d, 0.5d, new ActivationUniPolar());
        assertEquals(0, neat.size());
    }

    @Test
    public void errorDataLength(){
        int iterations = 2;
        NeatPopulation neat = new NeatPopulation(100, iterations, 0.001d, 0.5d, 0.5d, new ActivationUniPolar());
        GrayImageParser imageParser = new GrayImageParser("gray2.png", 64, false, false);
        double[][] output = neat.computeImage(imageParser.getNetworkInput(), 64, 32);
        double[] error = neat.getErrorData();

        assertEquals(iterations, error.length);
    }

    @Test
    public void errorDataTest(){
        int iterations = 2;
        NeatPopulation neat = new NeatPopulation(100, iterations, 0.001d, 0.5d, 0.5d, new ActivationUniPolar());
        GrayImageParser imageParser = new GrayImageParser("gray2.png", 64, false, false);
        double[][] output = neat.computeImage(imageParser.getNetworkInput(), 64, 32);
        double[] error = neat.getErrorData();

        neat.saveErrorToFile();
    }
}
