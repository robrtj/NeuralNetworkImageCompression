package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NeuralNetworkTest {

    @Test
    public void testFitnessFunction() throws Exception {

    }

    @Test
    public void testAddNode() throws Exception {
        Node a = new Node(1, LayerType.Input);
        Node b = new Node(10, LayerType.Intermediate);
        Node c = new Node(20, LayerType.Output);
        List<Node> nodes = new ArrayList<>();
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        NeuralNetwork nn = new NeuralNetwork(nodes, new ArrayList<Connection>());
        nn.addNode(a);
        nodes.add(2, new Node(5, LayerType.Compression));
        assertEquals(nodes.size(), nn.get_nodes().size());
        assertEquals(nodes.get(0), nn.get_nodes().get(0));
        assertEquals(nodes.get(3), nn.get_nodes().get(2));
    }

    @Test
    public void testCreatingNeuralNetwork() throws Exception {
//        NeuralNetwork net = new NeuralNetwork(2, 1);
//        net.sortNodeById();
//        assertEquals(5, net.get_nodes().size());
//        assertEquals(0, net.getNode(0).getInputConnections().size());
//        assertEquals(2, net.getNode(2).getInputConnections().size());
//
//        net = new NeuralNetwork(2, 2);
//        assertEquals(6, net.get_nodes().size());
//        assertEquals(0, net.getNode(0).getInputConnections().size());
//        assertEquals(2, net.getNode(2).getInputConnections().size());
    }

    @Test
    public void testSortNodeById(){

    }
}