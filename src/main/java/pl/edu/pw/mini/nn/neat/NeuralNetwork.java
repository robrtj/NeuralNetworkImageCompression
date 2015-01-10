package pl.edu.pw.mini.nn.neat;

import java.util.*;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork {
    private List<Node> _nodes;
    InnovationIdGenerator innovationNumberGenerator = new InnovationIdGenerator();
    private int inputLayerSize;
    private int intermediateLayerSize;

    NeuralNetwork(){
        _nodes = new LinkedList<>();
        innovationNumberGenerator = new InnovationIdGenerator();
    }

    NeuralNetwork(List<Node> nodes, List<Connection> connections) {
        this();
        _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        for (Connection connection : connections){
            Node node = connection.getOut();
            node.addConnection(connection);
        }

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

    //assume that nodes are in order:
    //In - Middle - Out
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
        FoundConnectionWrapper wrapper = checkConnectionExists(newConnection);
        if(!wrapper.found){
            newConnection.setInnovationNumber(innovationNumberGenerator.generate());
            newConnection.getOut().addConnection(newConnection);
        }
        else {
            updateConnection(wrapper.connection, newConnection);
        }
    }

    private FoundConnectionWrapper checkConnectionExists(Connection connection) {
        Node inNode = connection.getIn();
        Node outNode = connection.getOut();

        FoundConnectionWrapper wrapper;
        for (Connection inConnection : inNode.getInputConnections())
            if (inConnection.getOut() == outNode) {
                return new FoundConnectionWrapper(true, inConnection);
            }
        return new FoundConnectionWrapper(false, null);
    }

    public double fitnessFunction(double[][] input) {
        double fitness = 0.0d;
        for (double[] anInput : input) {
            compute(anInput);
            fitness += computeError();
        }
        return fitness;
    }

    //assume that input nodes are on the begin of list _nodes
    //and output nodes are at the end of list
    //otherwise we must find all input and output nodes before
    private double computeError() {
        double error = 0.0d;
        int size = _nodes.size();
        for (int i = 0; i < inputLayerSize; i++) {
            double inValue = _nodes.get(i).getWeight();
            double outValue = _nodes.get(size - inputLayerSize + i).getWeight();
            error += Math.pow(inValue-outValue, 2);
        }
        return error;
    }

    //assume that input is in activation function domain
    private void compute(double[] input) {
        sortNodeById();

        //set input values
        for (int i = 0; i < inputLayerSize; i++) {
            Node node = getNode(i);
            node.addWeight(input[i]);
        }

        //compute rest nodes
        for (int i = inputLayerSize; i < _nodes.size(); i++) {
            Node node = getNode(i);
            List<Connection> nodeInputs = node.getInputConnections();
            for (Connection conn : nodeInputs) {
                node.addWeight(conn.getWeight() * conn.getIn().getWeight());
            }
        }
    }

    public void sortNodeById() {
        _nodes.sort(new NodeByIdComparator());
    }

    public List<Node> get_nodes() {
        return _nodes;
    }

    public void updateConnection(Connection connection, Connection updatedConnection) {
        connection.setWeight(updatedConnection.getWeight());
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

    public double getLastIntermediateLayerNodeId() {
        return inputLayerSize + intermediateLayerSize;
    }

    public int getNumberOfNodes() {
        return _nodes.size();
    }


    class NodeByIdComparator implements Comparator<Node> {
        @Override
        public int compare(Node a, Node b) {
            double diff = a.getId() - b.getId();
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }
    }

    class FoundConnectionWrapper {
        private final Connection connection;
        private final boolean found;

        FoundConnectionWrapper(boolean found, Connection connection){
            this.found = found;
            this.connection = connection;
        }
    }
}
