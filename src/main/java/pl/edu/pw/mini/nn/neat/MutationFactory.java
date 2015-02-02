package pl.edu.pw.mini.nn.neat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactory {

    private Map<MutationType, Double> mutationTypes;

    private Random randGenerator = new Random();

    public MutationFactory() {
        mutationTypes = new HashMap<>();
        mutationTypesInitialization();

        setThresholds(0.3, 0.18, 0.04, 0.4, 0.08);
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
    public void setThresholds(double addConnection, double addNode,
                              double deleteConnection, double weightMutation,
                              double deleteNode) {
        mutationTypes.replace(MutationType.AddConnection, addConnection);
        mutationTypes.replace(MutationType.AddNode, addNode);
        mutationTypes.replace(MutationType.DeleteConnection, deleteConnection);
        mutationTypes.replace(MutationType.WeightMutation, weightMutation);
        mutationTypes.replace(MutationType.DeleteNode, deleteNode);

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
            case DeleteNode:
                mutated = deleteNode(net);
                break;
        }
        return mutated;
    }

    private boolean deleteNode(NeuralNetwork net) {
        int nodeCounter = net.getNumberOfNodes();
        int sample = -1;
        boolean isMiddle = false;
        for (int i = 0; i < 10; i++) {
            sample = randGenerator.nextInt(nodeCounter);
            if( checkIfMiddleNode(net, sample)){
                isMiddle = true;
                break;
            }
        }

        if(isMiddle ) {
            net.deleteNode(sample);
        }
        return isMiddle;
    }

    private boolean checkIfMiddleNode(NeuralNetwork net, int id) {
        Node node = net.getNode(id);
        return node.getLayerType() == LayerType.Compression
                || node.getLayerType() == LayerType.Decompression;
    }

    private boolean weightMutation(NeuralNetwork net) {
        int nodeCounter = net.getNumberOfNodes();
        int sample = randGenerator.nextInt(nodeCounter);

        Node node = net.getNode(sample);
        node.updateRandomConnection();
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

        if (conn == null) {
            return false;
        }

        double firstMiddle = net.getLastInputLayerNodeId() + 1;
        double firstOutput = net.getLastIntermediateLayerNodeId() + 1;
        double start = conn.getFromId();
        double stop = conn.getToId();
        LayerType fromLayer = conn.getFrom().getLayerType();
        LayerType toLayer = conn.getTo().getLayerType();
        switch (fromLayer) {
            case Input:
                start = firstMiddle - 1;
                break;
            case Intermediate:
                start = firstOutput - 1;
                break;
        }
        switch (toLayer) {
            case Intermediate:
                stop = firstMiddle;
                break;
            case Output:
                stop = firstOutput;
                break;
        }

        double id = 0;
        do {
            id = start + (stop - start) * randGenerator.nextDouble();
        } while (net.getNodeById(id) != null);

        LayerType layerType = conn.getToId() <= net.getLastIntermediateLayerNodeId()
                ? LayerType.Compression : LayerType.Decompression;
        Node middleNode = new Node(id, layerType);

        //dodanie wierzcholka i polaczen
        Connection inConn = new Connection(conn.getFrom(), middleNode, conn.getWeight(), true);
        Connection outConn = new Connection(middleNode, conn.getTo(), 1, true);
        //middleNode.addConnection(inConn);

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

        if (checkCorrectnessOfConnection(net, connection)) {
            net.addConnection(connection);
            return true;
        }
        return false;
    }

    //private
    public boolean checkCorrectnessOfConnection(NeuralNetwork net, Connection connection) {
        if (connection.getFromId() >= connection.getToId()) {
            return false;
        }

        LayerType fromLayerType = connection.getFrom().getLayerType();
        LayerType toLayerType = connection.getTo().getLayerType();

        switch (fromLayerType){
            case Input:
                if(toLayerType == LayerType.Compression || toLayerType == LayerType.Intermediate) {
                    return true;
                }
                return false;
            case Compression:
                if(toLayerType == LayerType.Compression || toLayerType == LayerType.Intermediate) {
                    return true;
                }
                return false;
            case Intermediate:
                if(toLayerType == LayerType.Decompression || toLayerType == LayerType.Output) {
                    return true;
                }
                return false;
            case Decompression:
                if(toLayerType == LayerType.Decompression || toLayerType == LayerType.Output) {
                    return true;
                }
                return false;
            case Output:
                return false;
            default:
                return false;
        }
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
}
