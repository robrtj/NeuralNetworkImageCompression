package pl.edu.pw.mini.nn.neat;

import org.junit.Test;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

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
}
