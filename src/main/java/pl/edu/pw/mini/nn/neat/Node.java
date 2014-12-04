package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Node {
    private int id;
    private LayerType layerType;

    public Node(int id, LayerType layerType) {
        this.id = id;
        this.layerType = layerType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LayerType getLayerType() {
        return layerType;
    }

    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
    }

    @Override
    public String toString(){
        return "Node " + id;
    }
}

