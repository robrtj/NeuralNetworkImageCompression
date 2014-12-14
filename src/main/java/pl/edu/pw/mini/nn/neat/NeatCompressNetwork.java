package pl.edu.pw.mini.nn.neat;

import org.encog.neural.neat.training.NEATInnovation;
import org.encog.util.identity.BasicGenerateID;
import org.encog.util.identity.GenerateID;

/**
 * Created by pawel.bielicki on 2014-12-13.
 */
public class NeatCompressNetwork {
    private NeatPopulation population;

    public NeatCompressNetwork(GenerateID innovationNumber, GenerateID nodeNumber) {
        innovationNumber = new BasicGenerateID();
        nodeNumber = new BasicGenerateID();
        population = new NeatPopulation(innovationNumber, nodeNumber);
    }

    public void iteration(){

    }
}
