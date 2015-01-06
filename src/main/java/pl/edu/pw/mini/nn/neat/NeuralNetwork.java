package pl.edu.pw.mini.nn.neat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork {
    private List<Node> _nodes;
    private List<Connection> _connections;
    InnovationNumber innovationNumberGenerator = new InnovationNumber();
    private int inputLayerSize;
    private int intermediateLayerSize;

    NeuralNetwork(){
        innovationNumberGenerator = new InnovationNumber();
    }

    NeuralNetwork(List<Node> nodes, List<Connection> connections) {
        this();
        _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _connections = new ArrayList<>();
        _connections.addAll(connections);

        for (Node node : _nodes) {
            if (node.getLayerType() == LayerType.Input) {
                inputLayerSize = (int) node.getId();
            } else if (node.getLayerType() == LayerType.Intermediate) {
                intermediateLayerSize = (int) node.getId();
            }
        }
    }

    //TODO
    public NeuralNetwork(int inputSize, int intermediateLayerSize) {
        this();
        this.inputLayerSize = inputSize;
        this.intermediateLayerSize = intermediateLayerSize;
    }

    void addNode(Node newNode) {
        _nodes.add(newNode);
    }

    void addConnection(Connection newConnection) {
        upsertConnection(newConnection);
    }

    //TODO
    //sprawdzenie, czy krawedz juz istnieje,
    //by bylo szybciej zamienic pola w krawedzi
    //oznaczajace id wierzcholka na referencje
    private void upsertConnection(Connection newConnection) {
        int index = findConnection(newConnection);
        if(index == -1){
            newConnection.setInnovationNumber(innovationNumberGenerator.nextInnovationNumber());
            _connections.add(newConnection);
        }
        else {
            updateWeight(index, newConnection.getWeight());
        }
    }

    private int findConnection(Connection newConnection) {
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

    public boolean updateWeight(int index, double weight) {
        try {
            _connections.get(index).setWeight(weight);
        }
        catch (IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }

    public Node getNode(int index) {
        return _nodes.get(index);
    }

    public Node getNodeById(double id) {
        for (Node node : _nodes){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public void disableConnection(int index) {
        _connections.get(index).disable();
    }

    public double getLastIntermediateLayerNodeId() {
        return inputLayerSize + intermediateLayerSize;
    }
}
