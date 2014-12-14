package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Node {
    private long id;
    private LayerType layerType;

    //TODO
    //delete if unnecessary
    private int layerNumber;

    public Node(long id, LayerType layerType) {
        this.id = id;
        this.layerType = layerType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

