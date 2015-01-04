package pl.edu.pw.mini.nn.neat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactoryTest {

    @Test
    public void mutate() throws Exception {
        MutationFactory mutationFactory = new MutationFactory();
        mutationFactory.setThresholds(1, 1, 1, 1);

        Node a = new Node(1, LayerType.Input);
        Node b = new Node(10, LayerType.Intermediate);
        Node c = new Node(20, LayerType.Output);
        List<Node> nodes = new ArrayList<>();
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        NeuralNetwork nn = new NeuralNetwork(nodes, new ArrayList<Connection>());

        boolean mutated = mutationFactory.mutate(nn);
        assertEquals(1, 0);
    }

    @Test
    public void addConnection() throws Exception {
        assertEquals(1, 0);
    }

    @Test
    public void checkCorrectnessOfConnection() throws Exception {
        assertEquals(1, 0);
    }
}
