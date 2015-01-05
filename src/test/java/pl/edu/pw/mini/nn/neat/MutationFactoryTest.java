package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        InnovationNumber innovationNumber = new InnovationNumber();
        Connection conn1 = new Connection(node1.getId(), node2.getId(),
                0.5, true, innovationNumber.nextInnovationNumber());
        Connection conn2 = new Connection(node2.getId(), node3.getId(),
                0.5, true, innovationNumber.nextInnovationNumber());
        conns.add(conn1);
        conns.add(conn2);

        return new NeuralNetwork(nodes, conns);
    }

    private List<Double> getConnectionWeight(NeuralNetwork net){
        List<Double> weights = new LinkedList<>();
        List<Connection> connections = net.get_connections();
        for (int i = 0; i < connections.size(); i++) {
            Connection con = connections.get(i);
            weights.add(con.getWeight());
        }
        return weights;
    }

    @Test
    public void addConnection() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        int size = net.get_connections().size();

        for (int i = 0; i < 10; i++) {
            List<Double> weights = getConnectionWeight(net);
            boolean actual = mutationFactory.addConnection(net);
            if( actual == false){
                continue;
            }
            boolean expected = net.get_connections().size() == size + 1;
            if(!expected){
                List<Double> newWeights = getConnectionWeight(net);
                if(weights.size() != newWeights.size()){
                    expected = true;
                    break;
                }
                for (int j = 0; j < weights.size(); j++) {
                    if(!Objects.equals(weights.get(j), newWeights.get(j))){
                        expected = true;
                        break;
                    }
                }
            }
            assertEquals(expected, actual);
        }
    }

    @Test
    public void checkCorrectnessOfConnectionTRUE() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        for (int i = 0; i < net.get_connections().size(); i++) {
            boolean actual = mutationFactory.checkCorrectnessOfConnection(net, net.getConnection(i));
            assertEquals(true, actual);
        }
    }

    @Test
    public void checkCorrectnessOfConnectionFALSE() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        Connection con = net.getConnection(0);
        net.get_nodes().remove(0);
        net.get_nodes().add(0, new Node(1, LayerType.Output));
        boolean actual = mutationFactory.checkCorrectnessOfConnection(net, con);
        assertEquals(false, actual);

//        con = net.getConnection(0);
//        net.get_nodes().add(0, new Node(1, LayerType.Intermediate));
//        net.get_nodes().add(1, new Node(2, LayerType.Intermediate));
//        actual = mutationFactory.checkCorrectnessOfConnection(net, con);
//        assertEquals(false, actual);
    }

    @Test
    public void addNode() throws Exception {
        NeuralNetwork net = createSimpleNetwork();
        MutationFactory mutationFactory = new MutationFactory();

        for (int i = 0; i < net.get_connections().size(); i++) {
            boolean actual = mutationFactory.addNode(net);
            assertEquals(true, actual);
        }
    }
}
