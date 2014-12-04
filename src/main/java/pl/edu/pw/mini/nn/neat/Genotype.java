package pl.edu.pw.mini.nn.neat;

import java.util.List;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Genotype {
    private List<Node> NodeGenes;
    private List<Connector> ConnectorGenes;

    public Genotype(List<Node> nodeGenes, List<Connector> connectorGenes) {
        NodeGenes = nodeGenes;
        ConnectorGenes = connectorGenes;
    }

    public List<Node> getNodeGenes() {
        return NodeGenes;
    }

    public void setNodeGenes(List<Node> nodeGenes) {
        NodeGenes = nodeGenes;
    }

    public List<Connector> getConnectorGenes() {
        return ConnectorGenes;
    }

    public void setConnectorGenes(List<Connector> connectorGenes) {
        ConnectorGenes = connectorGenes;
    }

    public Genotype mutate(){
        return this;
    }

    public Genotype cross(Genotype parent){
        return this;
    }
}

