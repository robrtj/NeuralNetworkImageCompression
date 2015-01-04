package pl.edu.pw.mini.nn.neat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork {
    private List<Node> _nodes;
    private List<Connection> _connections;

    NeuralNetwork(List<Node> nodes, List<Connection> connections) {
        _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _connections = new ArrayList<>();
        _connections.addAll(connections);
    }

    //TODO
    public NeuralNetwork(int inputSize, int intermediateLayerSize) {
    }

    //TODO
    void addNode(Node a, Node b) {

    }

    //TODO
    void addConnection(Connection newConnection) {

    }

    //TODO
    public void mutation() {

    }

    //TODO
    public double fitnessFunction(double[][] input) {

        return 0.0;
    }

    public List<Node> get_nodes() {
        return _nodes;
    }

    public List<Connection> get_connections() {
        return _connections;
    }

}
