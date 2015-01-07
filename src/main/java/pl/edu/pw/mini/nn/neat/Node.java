package pl.edu.pw.mini.nn.neat;

import pl.edu.pw.mini.nn.neat.activationFunction.ActivationFunction;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Node {
    private double id;
    private LayerType layerType;
    private List<Connection> inputConnections;
    private ActivationFunction activationFunction;

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

    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
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

    public void addValue(double weight) {
        activationFunction.addInput(weight);
    }

    @Override
    public String toString() {
        return "Node " + id;
    }

    public void addConnection(Connection newConnection) {
        inputConnections.add(newConnection);
    }
}

