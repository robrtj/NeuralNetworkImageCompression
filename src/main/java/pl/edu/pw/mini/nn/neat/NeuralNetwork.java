package pl.edu.pw.mini.nn.neat;

import java.util.*;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork {
    private List<Node> _nodes;
    private List<Connection> _connections;
    InnovationIdGenerator innovationNumberGenerator = new InnovationIdGenerator();
    private int inputLayerSize;
    private int intermediateLayerSize;

    NeuralNetwork(){
        _nodes = new LinkedList<>();
        _connections = new LinkedList<>();
        innovationNumberGenerator = new InnovationIdGenerator();
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

    public NeuralNetwork(int inputSize, int intermediateLayerSize) {
        this();
        this.inputLayerSize = inputSize;
        this.intermediateLayerSize = intermediateLayerSize;

        _nodes.addAll(createInputLayer(inputSize));
        _nodes.addAll(createMiddleLayer(intermediateLayerSize, inputSize));
        _nodes.addAll(createOutputLayer(inputSize, intermediateLayerSize));

        createStartConnections();
    }

    //zakladamy, ze wierzcholki sa uporzadkowane
    // wedlug schematu: In - Middle - Out
    private void createStartConnections() {
        createInputMiddleLayersConnections();
        createMiddleOutputLayersConnections();
    }

    private void createMiddleOutputLayersConnections() {
        Random rand = new Random();
        int firstMiddleNodeId = inputLayerSize;
        int firstOutputNodeId = inputLayerSize + intermediateLayerSize;

        for (int i = 0; i < inputLayerSize; i++) {
            Node outNode = _nodes.get(i);
            for (int j = 0; j < intermediateLayerSize; j++) {
                Node middleNode = _nodes.get(firstMiddleNodeId + j);
                Connection conn = new Connection(middleNode, outNode,
                        rand.nextDouble(), true,
                        innovationNumberGenerator.generate());
                _connections.add(conn);
                outNode.addConnection(conn);
            }
        }
    }

    private void createInputMiddleLayersConnections() {
        Random rand = new Random();
        int firstInputNodeId = 0;
        int firstMiddleNodeId = inputLayerSize;

        for (int i = 0; i < intermediateLayerSize; i++) {
            Node middleNode = _nodes.get(firstMiddleNodeId + i);
            for (int j = 0; j < inputLayerSize; j++) {
                Node inNode = _nodes.get(firstInputNodeId + j);
                Connection conn = new Connection(inNode, middleNode,
                        rand.nextDouble(), true,
                        innovationNumberGenerator.generate());
                _connections.add(conn);
                middleNode.addConnection(conn);
            }
        }
    }

    private Collection<Node> createOutputLayer(int size, int intermediateLayerSize) {
        return createLayer(size, LayerType.Output, size+intermediateLayerSize);
    }

    private Collection<Node> createMiddleLayer(int size, int inputSize) {
        return createLayer(size, LayerType.Intermediate, inputSize);
    }

    private Collection<Node> createInputLayer(int size) {
        return createLayer(size, LayerType.Input, 0);
    }

    private Collection<Node> createLayer(int size, LayerType type, int startId){
        List<Node> nodes = new LinkedList<>();
        int id = startId;
        while (size>0){
            Node node = new Node(id++, type);
            nodes.add(node);
            size--;
        }
        return nodes;
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
            newConnection.setInnovationNumber(innovationNumberGenerator.generate());
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
