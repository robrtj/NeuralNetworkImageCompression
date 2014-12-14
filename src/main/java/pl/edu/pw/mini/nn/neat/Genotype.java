package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.GenerateID;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        super();
        innovationNumber = generateID;
    }

    public void mutation(){
        Random random = new Random();
        MutationType mutationType = MutationType.getMutationType(random.nextDouble());

        switch (mutationType){
            case AddConnection:
                AddConnection();
                break;
            case AddNode:
                AddNode();
                break;
            case DeleteConnection:
                DeleteConnection();
                break;
            default:
                break;
        }
    }

    private void AddNode() {
        
    }

    private void DeleteConnection() {

    }

    private void AddConnection() {

    }

    public FFCompressNetwork createFastForwardNetwork(){
        return new FFCompressNetwork();
    }
}
