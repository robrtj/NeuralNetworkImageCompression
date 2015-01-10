package pl.edu.pw.mini.nn.neat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactory {

    private Map<MutationType, Double> mutationTypes;
    private double ratio;

    private Random randGenerator = new Random();

    public MutationFactory() {
        mutationTypes = new HashMap<>();
        ratio = 0.5d;

        mutationTypesInitialization();
    }

    private void mutationTypesInitialization() {
        double threshold = 0.0d;
        int count = MutationType.values().length;
        for (MutationType type : MutationType.values()) {
            mutationTypes.put(type, (double) (1.0 / count));
        }
    }

    private void normalizeThresholds() {
        double sum = 0;
        for (MutationType key : mutationTypes.keySet()) {
            sum += mutationTypes.get(key);
        }
        for (MutationType key : mutationTypes.keySet()) {
            double value = mutationTypes.get(key) / sum;
            mutationTypes.replace(key, value);
        }
    }

    //should be generic somehow
    public void setThresholds(double addConnection, double addNode, double deleteConnection, double weightMutation) {
        mutationTypes.replace(MutationType.AddConnection, addConnection);
        mutationTypes.replace(MutationType.AddNode, addNode);
        mutationTypes.replace(MutationType.DeleteConnection, deleteConnection);
        mutationTypes.replace(MutationType.WeightMutation, weightMutation);

        normalizeThresholds();
    }

    public boolean mutate(NeuralNetwork net) {
        double sample = randGenerator.nextDouble();
        boolean mutated = false;

        MutationType type = getMutationType(sample);
        switch (type) {
            case AddConnection:
                mutated = addConnection(net);
                break;
            case AddNode:
                mutated = addNode(net);
                break;
            case DeleteConnection:
                mutated = disableConnection(net);
                break;
            case WeightMutation:
                mutated = weightMutation(net);
                break;
        }
        return mutated;
    }

    private boolean weightMutation(NeuralNetwork net) {
        int nodeCounter = net.getNumberOfNodes();
        int sample = randGenerator.nextInt(nodeCounter);
        double weight = randGenerator.nextDouble();

        Node node = net.getNode(sample);
        node.updateRandomConnection(weight);
        return true;
    }

    private boolean disableConnection(NeuralNetwork net) {
        int nodeCounter = net.getNumberOfNodes();
        int sample = randGenerator.nextInt(nodeCounter);

        Node node = net.getNode(sample);
        node.disableRandomConnection(sample);
        return true;
    }

    //private
    public boolean addNode(NeuralNetwork net) {
        int nodeCounter = net.getNumberOfNodes();
        int sample = randGenerator.nextInt(nodeCounter);
        Node node = net.getNode(sample);
        Connection conn = node.getRandomConnection();

        double id = conn.getInId() + (conn.getOutId() - conn.getInId()) * randGenerator.nextDouble();
        LayerType layerType = conn.getOutId() <= net.getLastIntermediateLayerNodeId()
                ? LayerType.Compression : LayerType.Decompression;
        Node middleNode = new Node(id, layerType);

        //dodanie wierzcholka i polaczen
        Connection inConn = new Connection(conn.getIn(), middleNode, conn.getWeight(), true);
        Connection outConn = new Connection(middleNode, conn.getOut(), 1, true);
        middleNode.addConnection(inConn);

        conn.disable();
        net.addNode(middleNode);
        net.addConnection(inConn);
        net.addConnection(outConn);
        return true;
    }

    //private
    public boolean addConnection(NeuralNetwork net) {
        int in = randGenerator.nextInt(net.get_nodes().size());
        int out = randGenerator.nextInt(net.get_nodes().size());
        Node inNodeId = net.get_nodes().get(in);
        Node outNodeId = net.get_nodes().get(out);

        Connection connection = new Connection(inNodeId, outNodeId,
                randGenerator.nextDouble(), true);

        if(checkCorrectnessOfConnection(net, connection)){
            net.addConnection(connection);
            return true;
        }
        return false;
    }

    //private
    public boolean checkCorrectnessOfConnection(NeuralNetwork net, Connection connection) {
        if(connection.getInId() >= connection.getOutId()){
            return false;
        }

        Node in = connection.getIn();
        Node out = connection.getOut();
        if( in.getLayerType() == LayerType.Output){
            return false;
        }
        if( out.getLayerType() == LayerType.Input){
            return false;
        }
        if( in.getLayerType() == LayerType.Intermediate
                && out.getLayerType() == LayerType.Intermediate ){
            return false;
        }
        return true;
    }

    private MutationType getMutationType(double sample) {
        double threshold = 0.0d;
        for (MutationType key : mutationTypes.keySet()) {
            threshold += mutationTypes.get(key);
            if (sample < threshold) {
                return key;
            }
        }
        return null;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
