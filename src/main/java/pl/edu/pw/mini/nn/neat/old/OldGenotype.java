package pl.edu.pw.mini.nn.neat.old;

import pl.edu.pw.mini.nn.neat.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pawel on 2014-12-04.
 */
public class OldGenotype {
    private List<Node> NodeGenes;
    private List<Connection> ConnectionGenes;

    private static int innovationNumber = -1;
    private double mutationRatio = 0.5;

    public OldGenotype(){
        NodeGenes = new ArrayList<Node>();
        ConnectionGenes = new ArrayList<Connection>();
    }

    public OldGenotype(List<Node> nodeGenes, List<Connection> connectionGenes) {
        this.NodeGenes = nodeGenes;
        this.ConnectionGenes = connectionGenes;
    }

    public OldGenotype(int input, int output){
        NodeGenes = new ArrayList<Node>();
        ConnectionGenes = new ArrayList<Connection>();

        for(int i=0; i<input; ++i){
            Node node = new Node(i, LayerType.Input);
            NodeGenes.add(node);
        }

        for(int i=0; i<output; ++i){
            Node node = new Node(i+input, LayerType.Output);
            NodeGenes.add(node);
        }

        Random random = new Random();
        int ordinalNumber = 0;
        for (int i=0; i<input; ++i){
            for(int j=0; j<output; ++j){
                Connection connection = new Connection(i, j, random.nextDouble(), true, ordinalNumber);
                ++ordinalNumber;
                ConnectionGenes.add(connection);
            }
        }
    }

    public List<Node> getNodeGenes() {
        return NodeGenes;
    }

    public void setNodeGenes(List<Node> nodeGenes) {
        NodeGenes = nodeGenes;
    }

    public List<Connection> getConnectionGenes() {
        return ConnectionGenes;
    }

    public void setConnectionGenes(List<Connection> connectionGenes) {
        this.ConnectionGenes = connectionGenes;
    }

    public Node getNode(int index){
        return NodeGenes.get(index);
    }

    public Connection getConnection(int index){
        return ConnectionGenes.get(index);
    }

    public OldGenotype mutate() {
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
        int nodeCounter = NodeGenes.size();

        int from = random.nextInt(nodeCounter);
        while(NodeGenes.get(from).getLayerType() != LayerType.Output){
            from = random.nextInt(nodeCounter);
        }

        int to = random.nextInt(nodeCounter);
        while(from == to){
            to = random.nextInt(nodeCounter);
            while(NodeGenes.get(to).getLayerType() != LayerType.Input){
                to= random.nextInt(nodeCounter);
            }
        }
        double weight = random.nextDouble();

        Connection connection = new Connection(from, to, weight, true, getNextInnovationNumber());
        ConnectionGenes.add(connection);
    }

    private void mutateAddNode() {
        Random random = new Random();
        int maxOrdinalNumber = ConnectionGenes.size();
        int nodeCounter = NodeGenes.size();

//        czy nowy neuron moze powstac z nieaktywnego polaczenia?
//        jesli tak, to while jest niepotrzebny!
        int connectionNumber = random.nextInt(maxOrdinalNumber);
        while (!ConnectionGenes.get(connectionNumber).isEnabled()) {
            connectionNumber = random.nextInt(maxOrdinalNumber);
        }

        Node node = new Node(nodeCounter, LayerType.Hidden);
        NodeGenes.add(node);

        Connection from = ConnectionGenes.get(connectionNumber);
        Connection to = ConnectionGenes.get(connectionNumber);
        ConnectionGenes.get(connectionNumber).setEnabled(false);

        from.setOut(node.getId());
        from.setWeight(1);
        from.setInnovationNumber(getNextInnovationNumber());
        ConnectionGenes.add(from);

        to.setIn(node.getId());
        to.setWeight(ConnectionGenes.get(connectionNumber).getWeight());
        to.setInnovationNumber(getNextInnovationNumber());
        ConnectionGenes.add(to);
    }

    private int getNextInnovationNumber() {
        return ++innovationNumber;
    }

    public static OldGenotype cross(OldGenotype parent1, OldGenotype parent2) {
        Random random = new Random();
        OldGenotype genotype = new OldGenotype();
        int connIterator = 0;

        //wylosuj gen i wkladaj do potomka dopoki sa geny sa tymi samymi polaczeniami
        boolean sameConnection = true;
        while(sameConnection){
            Connection conn1 = parent1.getConnection(connIterator);
            Connection conn2 = parent2.getConnection(connIterator);

            if(conn1.getInnovationNumber() != conn2.getInnovationNumber()) {
                sameConnection = false;
                break;
            }

            boolean chosen = random.nextBoolean();
            if(chosen){
                genotype.getNodeGenes().add(parent1.getNode(parent1.getConnection(connIterator).getIn()));
                genotype.getNodeGenes().add(parent1.getNode(parent1.getConnection(connIterator).getOut()));
                genotype.getConnectionGenes().add(parent1.getConnection(connIterator));
            }
            ++connIterator;
        }

        return genotype;
    }
}

