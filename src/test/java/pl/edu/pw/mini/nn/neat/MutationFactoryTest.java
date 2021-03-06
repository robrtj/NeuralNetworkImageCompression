package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactoryTest{

    private NeuralNetwork createSimpleNetwork() {
        Node node1 = new Node(1, LayerType.Input);
        Node node2 = new Node(2, LayerType.Intermediate);
        Node node3 = new Node(3, LayerType.Output);
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        List<Connection> conns = new ArrayList<>();
        InnovationIdGenerator innovationNumber = new InnovationIdGenerator();
        Connection conn1 = new Connection(node1, node2, 0.5, true);
        Connection conn2 = new Connection(node2, node3, 0.5, true);
        conn1.setInnovationNumber(innovationNumber.generate());
        conn2.setInnovationNumber(innovationNumber.generate());
        conns.add(conn1);
        conns.add(conn2);

        return new NeuralNetwork(nodes, conns);
    }

    private List<Double> getConnectionWeight(NeuralNetwork net){
        List<Double> weights = new LinkedList<>();
        for (Node node : net.get_nodes()){
            for (Connection conn : node.getInputConnections()){
                weights.add(conn.getWeight());
            }
        }
        return weights;
    }

    @Test
    public void checkCorrectnessOfConnectionTRUE() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        for (Node node : net.get_nodes()) {
            for (Connection conn : node.getInputConnections()) {
                boolean actual = mutationFactory.checkCorrectnessOfConnection(net, conn);
                assertEquals(true, actual);
            }
        }
    }

    @Test
    public void checkCorrectnessOfConnectionFALSE() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        Connection con = net.getNode(1).getConnection(0);
        Node a = new Node(con.getFrom());
        a.setLayerType(LayerType.Output);
        con.setFrom(a);
        net.get_nodes().remove(0);
        net.get_nodes().add(0, new Node(1, LayerType.Output));
        boolean actual = mutationFactory.checkCorrectnessOfConnection(net, con);
        assertEquals(false, actual);
    }

    @Test
    public void addNode() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        for (int i = 0; i < 10; i++) {
            int nodeCounter_before = net.getNumberOfNodes();
            int connCounter_before = 0;
            for(Node node : net.get_nodes()){
                connCounter_before += node.getNumberOfConnections();
            }
            List<Node> nodeIds = new LinkedList<>();
            nodeIds.addAll(net.get_nodes());

            boolean actual = mutationFactory.addNode(net);
            if(!actual){
                continue;
            }
            int nodeCounter_after = net.get_nodes().size();
            int connCounter_after = 0;
            for(Node node : net.get_nodes()){
                connCounter_after += node.getNumberOfConnections();
            }

            assertEquals(nodeCounter_before + 1, nodeCounter_after);
            assertEquals(connCounter_before + 2, connCounter_after);

            //check if new node is connected with new edges
            Node newNode = null;
            for(Node node : net.get_nodes()){
                if(!nodeIds.contains(node)){
                    newNode = node;
                    break;
                }
            }
            List<Connection> conns = newNode.getInputConnections();
            assertEquals(1, conns.size());
            Connection conn = newNode.getInputConnections().get(0);
            assertEquals(newNode.getId(), conn.getToId(), 0.0d);
            int outsCounter = 0;
            for(Node node : net.get_nodes()) {
                for (Connection con : node.getInputConnections()) {
                    if (con.getFromId() == newNode.getId()) {
                        outsCounter++;
                    }
                }
            }
            assertEquals(1, outsCounter);
        }
    }
}
