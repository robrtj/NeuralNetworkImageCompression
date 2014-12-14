package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.GenerateID;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Pawel on 2014-12-14.
 */
public class Genotype {
    private List<Connection> connections;
    private GenerateID innovationNumber;
    private GenerateID nodeNumber;

    private Genotype(){
        connections = new LinkedList<>();
    }

    public Genotype(GenerateID generateID){
        innovationNumber = generateID;
    }

    public FFCompressNetwork createFastForwardNetwork(){
        return new FFCompressNetwork();
    }
}
