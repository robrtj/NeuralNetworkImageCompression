package pl.edu.pw.mini.nn.neat;

import org.junit.Test;
import pl.edu.pw.mini.nn.neat.activationFunction.ActivationUniPolar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class NeuralNetworkTest {
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
    public void testCreateNeuralNetworkWithLayerSizes() {
        int inputSize = 3;
        int middleSize = 2;

        NeuralNetwork net = new NeuralNetwork(inputSize, middleSize);

        assertEquals(2 * inputSize + middleSize, net.getNumberOfNodes());
        assertEquals(inputSize + middleSize - 1, net.getLastIntermediateLayerNodeId(), 0.0d);
        for (Node node : net.get_nodes()) {
            if (node.getLayerType() == LayerType.Input) {
                assertEquals(0, node.getNumberOfConnections());
            } else if (node.getLayerType() == LayerType.Intermediate) {
                assertEquals(inputSize, node.getNumberOfConnections());
            } else if (node.getLayerType() == LayerType.Output) {
                assertEquals(middleSize, node.getNumberOfConnections());
            } else assertTrue(false);
        }
    }

    @Test
    public void testAddNewConnection() throws Exception {
        NeuralNetwork net = new NeuralNetwork(3, 2);
        Connection conn = new Connection(net.getNode(0), net.getNode(5), 0.1d, true, 12);
        net.addConnection(conn);

        int sum = 0;
        for (Node node : net.get_nodes()) {
            sum += node.getNumberOfConnections();
        }
        assertEquals(13, sum);
        assertEquals(3, net.getNodeById(5).getNumberOfConnections());
        assertEquals(2, net.getNodeById(6).getNumberOfConnections());
    }

    @Test
    public void testAddExistingConnection() throws Exception {
        NeuralNetwork net = new NeuralNetwork(3, 2);
        Connection conn = new Connection(net.getNodeById(0), net.getNodeById(3), 0.1d, true, 12);
        net.addConnection(conn);

        int sum = 0;
        for (Node node : net.get_nodes()) {
            sum += node.getNumberOfConnections();
        }
        assertEquals(12, sum);
        assertEquals(3, net.getNodeById(3).getNumberOfConnections());
        assertEquals(2, net.getNodeById(5).getNumberOfConnections());
        assertEquals(2, net.getNodeById(6).getNumberOfConnections());
        assertEquals(0.1d, net.getNodeById(3).getInputConnections().get(0).getWeight(), 0.0d);
    }

    @Test
    public void testUpdateConnection() throws Exception {
        NeuralNetwork net = new NeuralNetwork(3, 2);
        Connection conn = new Connection(net.getNodeById(0), net.getNodeById(3), 0.1d, true, 12);
        net.updateConnection(net.getNodeById(3).getInputConnections().get(0), conn);

        int sum = 0;
        for (Node node : net.get_nodes()) {
            sum += node.getNumberOfConnections();
        }
        assertEquals(12, sum);
        assertEquals(3, net.getNodeById(3).getNumberOfConnections());
        assertEquals(2, net.getNodeById(5).getNumberOfConnections());
        assertEquals(2, net.getNodeById(6).getNumberOfConnections());
        assertEquals(0.1d, net.getNodeById(3).getInputConnections().get(0).getWeight(), 0.0d);
    }

    @Test
    public void testFitnessFunction() throws Exception {
        NeuralNetwork net = new NeuralNetwork(2, 1);
        double tmp = 0.1;
        for (Node node : net.get_nodes()){
            for (Connection conn : node.getInputConnections()){
                conn.setWeight(tmp);
                tmp += 0.1;
            }
            if(LayerType.Input == node.getLayerType()){
                node.addWeight(0.5 + node.getId() *0.4);
            }
        }
        double[][] in = {{0.5, 0.9}};
        double fitness = net.fitnessFunction(in);
        double expected = 0.7;
        assertEquals(expected, fitness, 0.00001d);

        in = new double[][]{{0.1, 0.3}};
        fitness = net.fitnessFunction(in);
        expected = 0.4;
        assertEquals(expected, fitness, 0.00001d);

        in = new double[][]{{0.5, 0.9}, {0.1, 0.3}};
        fitness = net.fitnessFunction(in);
        expected = 1.1;
        assertEquals(expected, fitness, 0.00001d);

    }

    @Test
    public void testComputeError() throws Exception {
        NeuralNetwork net = new NeuralNetwork(3, 2, new ActivationUniPolar());

        for (Node node : net.get_nodes()){
            if(node.getLayerType() == LayerType.Input){
                node.addWeight(1);
            }
            if(node.getLayerType() == LayerType.Output){
                node.addWeight(1);
            }
        }
        net.sortNodeById();
        double error = net.computeError();
        assertEquals(1.5d, error, 0.001d);

        net = new NeuralNetwork(3, 2);
        for (Node node : net.get_nodes()){
            if(node.getLayerType() == LayerType.Input){
                node.addWeight(-1);
            }
            if(node.getLayerType() == LayerType.Output){
                node.addWeight(2);
            }
        }
        error = net.computeError();
        assertEquals(6, error, 0.0d);
    }

    @Test
    public void testCompute() throws Exception {
        int inputSize = 2;
        NeuralNetwork net = new NeuralNetwork(inputSize, 1);
        double tmp = 0.1;
        for (Node node : net.get_nodes()) {
            for (Connection conn : node.getInputConnections()) {
                conn.setWeight(tmp);
                tmp += 0.1;
            }
            if (LayerType.Input == node.getLayerType()) {
                node.addWeight(0.5 + node.getId() * 0.4);
            }
        }
        double[] in = {0.5, 0.9};
        net.compute(in);

        assertEquals(1, net.getNodeById(2).getWeight(), 0.0d);
        assertEquals(0.3, net.getNodeById(3).getWeight(), 0.000001d);
        assertEquals(0.4, net.getNodeById(4).getWeight(), 0.000001d);
    }

    @Test
    public void testSortNodeById() throws Exception {
        NeuralNetwork net = new NeuralNetwork(new LinkedList<Node>(), new LinkedList<Connection>());
        for (int i = 0; i < 10; i++) {
            Node node;
            if(i % 2 == 0) {
                node = new Node(10 - i, LayerType.Input);
            }else{
                node = new Node(i, LayerType.Input);
            }
            net.addNode(node);
        }
        net.sortNodeById();
        for (int i = 0; i < 10; i++) {
            assertEquals(i +1, net.getNode(i).getId(), 0.0d);
        }
    }

    @Test
    public void testCloneNet() throws Exception {
        NeuralNetwork net = new NeuralNetwork(3, 2);
        NeuralNetwork clone = net.clone();

        for (Node node : clone.get_nodes()){
            Node old = net.getNodeById(node.getId());
            assertNotEquals(node, old);
        }
    }
}