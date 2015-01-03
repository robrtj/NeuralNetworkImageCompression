package pl.edu.pw.mini.nn.neat;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork implements Comparable{
    private List<Node> _nodes;
    private List<Connection> _connections;

    private double fitness;

    public NeuralNetwork(){
        fitness = Double.POSITIVE_INFINITY;
    }

    NeuralNetwork(List<Node> nodes, List<Connection> connections) {
        this();
        _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _connections = new ArrayList<>();
        _connections.addAll(connections);
    }

    //TODO
    public NeuralNetwork(int inputSize, int intermediateLayerSize) {
        this();
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
    double fitnessFunction(double[][] input) {

        return 0.0;
    }

    public List<Node> get_nodes() {
        return _nodes;
    }

    public List<Connection> get_connections() {
        return _connections;
    }

    public double getFitness(){
        return fitness;
    }

    @Override
    public int compareTo(Object o) {
        NeuralNetwork net = (NeuralNetwork) o;
        if (fitness == net.fitness){
            return 0;
        }
        return fitness < net.fitness ? -1 : 1;
    }

    //TODO
    public void updateFitness() {
    }
}
