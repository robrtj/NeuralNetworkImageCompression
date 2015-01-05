package pl.edu.pw.mini.nn.neat;

import org.encog.util.identity.GenerateID;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pawel on 2014-12-14.
 */
public class Genotype {
    private oldNeatPopulation parent;
    private List<Connection> connections;
    private List<Node> nodes;
    private GenerateID innovationNumber;
    private GenerateID nodeNumber;

    private Genotype(){
        connections = new LinkedList<>();
        nodes = new LinkedList<>();
    }

    public Genotype(GenerateID InnovationGenerateID){
        super();
        innovationNumber = InnovationGenerateID;
    }

    public void mutation(){
//        Random random = new Random();
//        MutationType mutationType = null;
//
//        try {
//            mutationType = MutationType.getMutationType(random.nextDouble());
//            switch (mutationType){
//                case AddConnection:
//                    AddConnection();
//                    break;
//                case AddNode:
//                    AddNode();
//                    break;
//                case WeightMutation:
//                    WeightMutation();
//                    break;
//                case DeleteConnection:
//                    DeleteConnection();
//                    break;
//                default:
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void WeightMutation() {
        Random rand = new Random();
        int index = rand.nextInt(connections.size());
        connections.get(index).setWeight(rand.nextDouble());
    }

    private void AddNode() {
//        Random rand = new Random();
//        long newNodeNumber = nodeNumber.generate();
//
//        Connection connection;
//        do {
//            connection = connections.get(rand.nextInt(connections.size()));
//        } while (!connection.isEnabled());
//
//        Connection inConn = connection.clone();
//        inConn.setOut(newNodeNumber);
//        inConn.setWeight(1.0d);
//
//        Connection outConn = connection.clone();
//        outConn.setIn(newNodeNumber);
//        outConn.setWeight(rand.nextDouble());
//
//        connection.disable();
//
//        Node node = new Node(newNodeNumber, LayerType.Hidden);
//
//        connections.add(inConn);
//        connections.add(outConn);
//        nodes.add(node);
    }

    private void DeleteConnection() {
        Random rand = new Random();
        int index = rand.nextInt(connections.size());
        connections.get(index).setEnabled(false);
    }

    private void AddConnection() {
        Random rand = new Random();
        long innovation = innovationNumber.generate();

        int from = rand.nextInt(nodes.size());
        while(isOutputNode(from)){
            from = rand.nextInt();
        }

        int to = rand.nextInt(nodes.size());
        while(isInputNode(to)){
            to = rand.nextInt();
        }

        Connection connection = new Connection(from, to, rand.nextDouble(), true, innovation);
        connections.add(connection);
    }

    private boolean isInputNode(int from) {
        return nodes.get(from).getLayerType() == LayerType.Input;
    }
    private boolean isHiddenNode(int from) {
//        return nodes.get(from).getLayerType() == LayerType.Hidden;
        return false;
    }
    private boolean isOutputNode(int from) {
        return nodes.get(from).getLayerType() == LayerType.Output;
    }

    public FFCompressNetwork createFastForwardNetwork(){
        return new FFCompressNetwork();
    }
}
