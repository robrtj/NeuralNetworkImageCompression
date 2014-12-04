package pl.edu.pw.mini.nn.neat;

import java.util.List;
import java.util.Random;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Genotype {
    private List<Node> NodeGenes;
    private List<Connector> ConnectorGenes;

    private double mutationRatio = 0.5;

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

    public Genotype mutate() {
        Random random = new Random();
        double mutationType = random.nextDouble();

        if (mutationType <= mutationRatio) {
            mutateAddConnection();
        } else {
            mutateAddNode();
        }

        return this;
    }

    private void mutateAddConnection(){
        Random random = new Random();
        int maxOrdinalNumber = ConnectorGenes.size();
        int nodeCounter = NodeGenes.size();

        int from = random.nextInt(nodeCounter);
        int to = random.nextInt(nodeCounter);
        while(from == to){
            to = random.nextInt(nodeCounter);
        }
        double weight = random.nextDouble();

        Connector connector = new Connector(from, to, weight, true, maxOrdinalNumber);
        ConnectorGenes.add(connector);
    }

    private void mutateAddNode() {
        Random random = new Random();
        int maxOrdinalNumber = ConnectorGenes.size();
        int nodeCounter = NodeGenes.size();

        int connectionNumber = random.nextInt(maxOrdinalNumber);
        while(!ConnectorGenes.get(connectionNumber).isEnabled()){
            connectionNumber = random.nextInt(maxOrdinalNumber);
        }

        Node node = new Node(nodeCounter, LayerType.Hidden);
        NodeGenes.add(node);

        Connector from = ConnectorGenes.get(connectionNumber);
        Connector to = ConnectorGenes.get(connectionNumber);
        ConnectorGenes.get(connectionNumber).setEnabled(false);

        from.setOut(node.getId());
        from.setWeight(random.nextDouble());
        from.setOrdinalNumber(maxOrdinalNumber+1);
        ConnectorGenes.add(from);

        to.setIn(node.getId());
        to.setWeight(random.nextDouble());
        to.setOrdinalNumber(maxOrdinalNumber+2);
        ConnectorGenes.add(to);
    }

    public Genotype cross(Genotype parent) {
        return this;
    }
}

