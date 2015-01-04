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

    void addConnection(Connection newConnection) {
        upsertConnection(newConnection);
    }

    private void upsertConnection(Connection newConnection) {
        int index = getConnectionIndex(newConnection);
        if(index == -1){
            _connections.add(newConnection);
        }
        else {
            _connections.add(index, newConnection);
        }
    }

    private int getConnectionIndex(Connection newConnection) {
        int index = -1;
        for (int i = 0; i < _connections.size(); i++) {
            Connection conn = getConnection(i);
            if(conn.getIn() == newConnection.getIn() && conn.getOut() == newConnection.getOut()){
                index = i;
                break;
            }
        }
        return index;
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

    public Connection getConnection(int index) {
        return _connections.get(index);
    }

    public void updateWeight(int index, double weight) {
        _connections.get(index).setWeight(weight);
    }

    public Node getNode(int index) {
        return _nodes.get(index);
    }
}
