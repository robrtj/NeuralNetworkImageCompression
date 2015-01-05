package pl.edu.pw.mini.nn.neat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Node {
    private double id;
    private LayerType layerType;
    private List<Connection> inputConnections;
    private int weight;

    public Node(int id, LayerType layerType) {
        this.id = id;
        this.layerType = layerType;
        this.inputConnections = new ArrayList<>();
    }

    public Node(int id, LayerType layerType, List<Connection> inputConnections) {
        this(id, layerType);
        this.inputConnections.addAll(inputConnections);
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Node " + id;
    }
}

