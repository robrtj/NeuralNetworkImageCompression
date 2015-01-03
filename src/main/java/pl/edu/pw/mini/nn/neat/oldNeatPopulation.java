package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.BasicGenerateID;
import org.encog.util.identity.GenerateID;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pawel.bielicki on 2014-12-14.
 */
public class oldNeatPopulation {
    private NeatCompressNetwork parent;
    private List<Genotype> individuals;
    private GenerateID innovationNumber;
    private GenerateID nodeNumber;

    public oldNeatPopulation(){
        individuals = new LinkedList<>();
        this.innovationNumber = new BasicGenerateID();
        this.nodeNumber = new BasicGenerateID();
    }

    public oldNeatPopulation(NeatCompressNetwork neatCompressNetwork) {
        super();
        parent = neatCompressNetwork;
    }

    public Genotype getBestGenotype(){
        return null;
    }
}
