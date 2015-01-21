package pl.edu.pw.mini.nn.neat;

import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.util.*;

/**
 * Created by Robert on 2015-01-03.
 */
public class NeuralNetwork {
    private List<Node> _nodes;
    private InnovationIdGenerator innovationNumberGenerator = new InnovationIdGenerator();
    private int inputLayerSize;
    private int intermediateLayerSize;
    private ActivationFunction activationFunction;

    NeuralNetwork() {
        _nodes = new LinkedList<>();
        innovationNumberGenerator = new InnovationIdGenerator();
        activationFunction = new ActivationUniPolar();
    }

    //assume inputConnections in node is empty
    NeuralNetwork(List<Node> nodes, List<Connection> connections) {
        this();
        _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        for (Connection connection : connections) {
            Node node = connection.getTo();
            node.addConnection(connection);
        }

        for (Node node : _nodes) {
            int id = (int) node.getId();
            if (node.getLayerType() == LayerType.Input) {
                inputLayerSize = inputLayerSize > id ? inputLayerSize : id;
            } else if (node.getLayerType() == LayerType.Intermediate) {
                intermediateLayerSize = intermediateLayerSize > id ? intermediateLayerSize : id;
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

    public NeuralNetwork(int inputLayerSize, int middleLayerSize, ActivationFunction activationFunction) {
        this(inputLayerSize, middleLayerSize);
        setActivationFunction(activationFunction);
    }

    //assume that node ids are in order:
    //In - Middle - Out
    private void createStartConnections() {
        sortNodeById();
        createStartInputMiddleLayersConnections();
        createStartMiddleOutputLayersConnections();
    }

    private void createStartMiddleOutputLayersConnections() {
        Random rand = new Random();
        int firstMiddleNodeId = inputLayerSize;
        int firstOutputNodeId = inputLayerSize + intermediateLayerSize;

        for (int i = 0; i < inputLayerSize; i++) {
            Node outNode = _nodes.get(firstOutputNodeId + i);
            for (int j = 0; j < intermediateLayerSize; j++) {
                Node middleNode = _nodes.get(firstMiddleNodeId + j);
                Connection conn = new Connection(middleNode, outNode,
                        rand.nextDouble() - 0.5d, true,
                        innovationNumberGenerator.generate());
                outNode.addConnection(conn);
            }
        }
    }

    private void createStartInputMiddleLayersConnections() {
        Random rand = new Random();
        int firstInputNodeId = 0;
        int firstMiddleNodeId = inputLayerSize;

        for (int i = 0; i < intermediateLayerSize; i++) {
            Node middleNode = _nodes.get(firstMiddleNodeId + i);
            for (int j = 0; j < inputLayerSize; j++) {
                Node inNode = _nodes.get(firstInputNodeId + j);
                Connection conn = new Connection(inNode, middleNode,
                        rand.nextDouble() - 0.5d, true,
                        innovationNumberGenerator.generate());
                middleNode.addConnection(conn);
            }
        }
    }

    private List<Node> createOutputLayer(int size, int intermediateLayerSize) {
        return createLayer(size, LayerType.Output, size + intermediateLayerSize);
    }

    private List<Node> createMiddleLayer(int size, int inputSize) {
        return createLayer(size, LayerType.Intermediate, inputSize);
    }

    private List<Node> createInputLayer(int size) {
        return createLayer(size, LayerType.Input, 0);
    }

    private static List<Node> createLayer(int size, LayerType type, int startId) {
        List<Node> nodes = new LinkedList<>();
        int id = startId;
        while (size > 0) {
            Node node = new Node(id++, type);
            nodes.add(node);
            size--;
        }
        return nodes;
    }

    public void addNode(Node newNode) {
        newNode.setActivationFunction(activationFunction);
        _nodes.add(newNode);
    }

    public void addConnection(Connection newConnection) {
        Connection existingConnection = checkConnectionExists(newConnection);
        if (existingConnection == null) {
            newConnection.setInnovationNumber(innovationNumberGenerator.generate());
            newConnection.getTo().addConnection(newConnection);
        } else {
            updateConnection(existingConnection, newConnection);
        }
    }

    private Connection checkConnectionExists(Connection connection) {
        Node inNode = connection.getFrom();
        Node outNode = connection.getTo();
        for (Connection inConnection : outNode.getInputConnections())
            if (inConnection.getFrom() == inNode) {
                return inConnection;
            }
        return null;
    }

    public void updateConnection(Connection connection, Connection updatedConnection) {
        connection.setWeight(updatedConnection.getWeight());
    }

    public double fitnessFunction(double[][] input) {
        sortNodeById();

        double fitness = 0.0d;
        for (double[] anInput : input) {
            compute(anInput);
            fitness += computeError();
        }
        return fitness;
    }

    //assume input nodes are at the start of _nodes
    //and output nodes are at the end of _nodes
    //otherwise we must rewrite algorithm to filter input and output nodes before
    public double computeError() {
        double error = 0.0d;
        int size = _nodes.size();
        for (int i = 0; i < inputLayerSize; i++) {
            double inValue = _nodes.get(i).getWeight();
            double outValue = _nodes.get(size - inputLayerSize + i).getWeight();
            error += Math.abs(inValue - outValue);
        }
        return error;
    }

    //assume input is in activation function domain
    public void compute(double[] input) {
        resetNetwork();

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
                node.addWeight(conn.getWeight() * conn.getFrom().getWeight());
            }
        }
    }

    private void resetNetwork() {
        for (Node node : _nodes){
            node.resetWeight();
        }
    }

    public void sortNodeById() {
        _nodes.sort(new NodeByIdComparator());
    }

    public List<Node> get_nodes() {
        return _nodes;
    }

    public Node getNode(int index) {
        return _nodes.get(index);
    }

    public Node getNodeById(double id) {
        for (Node node : _nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public double getLastInputLayerNodeId() {
        return inputLayerSize - 1;
    }

    public double getLastIntermediateLayerNodeId() {
        return inputLayerSize + intermediateLayerSize - 1;
    }

    public int getNumberOfNodes() {
        return _nodes.size();
    }

    public double[][] getCompressedVector(double[][] image) {
        sortNodeById();

        double[][] compressed = new double[image.length][];
        for (int i = 0, imageLength = image.length; i < imageLength; i++) {
            double[] anInput = image[i];
            compute(anInput);
            //get middle vector
            for (int j = inputLayerSize+1; j < intermediateLayerSize; j++) {
                compressed[i][j] = getNodeById(j).getCompressedOutput();
            }
        }
        return compressed;
    }

    public double[][] getNetworkOutput(double[][] input) {
        sortNodeById();
        double[][] output = new double[input.length][inputLayerSize];

        for (int i = 0; i < input.length; i++) {
            compute(input[i]);

            int firstOutputNeuronIndex = _nodes.size() - inputLayerSize;
            for (int j = 0; j < inputLayerSize; j++) {
                output[i][j] = _nodes.get(j + firstOutputNeuronIndex).getWeight();
            }
        }

        return output;
    }

    public List<Connection> getConnections() {
        List<Connection> conns = new ArrayList<>();
        for(Node node : _nodes){
            conns.addAll(node.getInputConnections());
        }
        return conns;
    }

    public void setLayerSizes(int inputLayerSize, int intermediateLayerSize) {
        this.inputLayerSize = inputLayerSize;
        this.intermediateLayerSize = intermediateLayerSize;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        for(Node node : _nodes){
            node.setActivationFunction(activationFunction);
        }
    }


    class NodeByIdComparator implements Comparator<Node> {
        @Override
        public int compare(Node a, Node b) {
            double diff = a.getId() - b.getId();
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }
    }
}
