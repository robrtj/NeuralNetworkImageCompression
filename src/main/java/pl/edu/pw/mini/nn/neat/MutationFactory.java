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
            mutationTypes.put(type, (double) (1 / count));
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
        int connectionCounter = net.get_connections().size();
        int sample = randGenerator.nextInt(connectionCounter);

        double weight = randGenerator.nextDouble();
        net.updateWeight(sample, weight);
        return true;
    }

    private boolean disableConnection(NeuralNetwork net) {
        int connectionCounter = net.get_connections().size();
        int sample = randGenerator.nextInt(connectionCounter);

        net.disableConnection(sample);
        return true;
    }

    //private
    public boolean addNode(NeuralNetwork net) {
        int connectionCounter = net.get_connections().size();
        int index = randGenerator.nextInt(connectionCounter);
        Connection conn = net.getConnection(index);

        double id = conn.getIn() + (conn.getOut() - conn.getIn()) * randGenerator.nextDouble();
        LayerType layerType = conn.getOut()<=net.getLastIntermediateLayerNodeId()
                ? LayerType.Compression : LayerType.Decompression;
        Node middleNode = new Node(id, layerType);

        //dodanie wierzcholka i polaczen
        Connection inConn = new Connection(conn.getIn(), id, conn.getWeight(), true);
        Connection outConn = new Connection(id, conn.getOut(), 1, true);
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
        double inNodeId = net.get_nodes().get(in).getId();
        double outNodeId = net.get_nodes().get(out).getId();

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
        if(connection.getIn() >= connection.getOut()){
            return false;
        }

        Node in = net.getNodeById(connection.getIn());
        Node out = net.getNodeById(connection.getOut());
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
