package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.GenerateID;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pawel.bielicki on 2014-12-14.
 */
public class NeatPopulation {
    private List<Genotype> individuals;
    private GenerateID innovationNumber;
    private GenerateID nodeNumber;

    public NeatPopulation(GenerateID innovationNumber, GenerateID nodeNumber){
        individuals = new LinkedList<>();
        this.innovationNumber = innovationNumber;
        this.nodeNumber = nodeNumber;
    }

    public Genotype getBestGenotype(){
        return null;
    }
}
