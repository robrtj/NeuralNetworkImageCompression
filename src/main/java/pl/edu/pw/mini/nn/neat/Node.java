package pl.edu.pw.mini.nn.neat;

import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Node {
    private double id;
    private LayerType layerType;
    private List<Connection> inputConnections;
    private ActivationFunction activationFunction;

    private Random randomGenerator = new Random();

    public Node(double id, LayerType layerType) {
        this.id = id;
        this.layerType = layerType;
        this.inputConnections = new ArrayList<>();
        activationFunction = new ActivationUniPolar();
    }

    public Node(double id, LayerType layerType, List<Connection> inputConnections, ActivationFunction activationFunction) {
        this(id, layerType);
        this.inputConnections.addAll(inputConnections);
        this.activationFunction = activationFunction;
    }

    public Node(Node node) {
        this(node.id, node.getLayerType(), node.getInputConnections(), node.getActivationFunction());
    }

    public double getId() {
        return id;
    }

    public LayerType getLayerType() {
        return layerType;
    }

    public List<Connection> getInputConnections() {
        return inputConnections;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public double getWeight() {
        return activationFunction.getValue();
    }

    public void addWeight(double weight) {
        activationFunction.addInput(weight);
    }

    @Override
    public String toString() {
        return "Node " + id;
    }

    public void addConnection(Connection newConnection) {
        inputConnections.add(newConnection);
    }

    public void updateRandomConnection(double weight) {
        int size = inputConnections.size();
        if (size > 0) {
            Connection conn = inputConnections.get(randomGenerator.nextInt(inputConnections.size()));
            conn.setWeight(weight);
        }
    }

    public void disableRandomConnection(int sample) {
        int size = inputConnections.size();
        if (size > 0) {
            Connection conn = inputConnections.get(randomGenerator.nextInt(inputConnections.size()));
            conn.disable();
        }
    }

    public Connection getRandomConnection() {
        int size = inputConnections.size();
        if (size > 0) {
            return inputConnections.get(randomGenerator.nextInt(inputConnections.size()));
        }
        return null;
    }

    public Connection getConnection(int index) {
        return index < inputConnections.size() ? inputConnections.get(index) : null;
    }
}

