package pl.edu.pw.mini.nn.neat;

import java.util.*;

/**
 * Created by Pawel on 2015-01-11.
 */
public class CrossoverFactory {
    Random randomGenerator = new Random();

    public NeuralNetwork  cross(NeuralNetwork firstParent, NeuralNetwork secondParent) {
        List<Connection> f_parent = firstParent.getConnections();
        List<Connection> s_parent = secondParent.getConnections();
        f_parent.sort(new ConnectionByIdComparator());
        s_parent.sort(new ConnectionByIdComparator());

        List<Connection> child = new LinkedList<>();
        int f_iterator = 0;
        int s_iterator = 0;

        while (f_iterator < f_parent.size() && s_iterator < s_parent.size()) {
            Connection f_conn = f_parent.get(f_iterator);
            Connection s_conn = s_parent.get(s_iterator);
            double diff = f_conn.getInnovationNumber() - s_conn.getInnovationNumber();
            if (diff < 0) {
                child.add(f_conn);
                f_iterator++;
            } else if (diff > 0) {
                child.add(s_conn);
                s_iterator++;
            } else {
                if (randomGenerator.nextDouble() < 0.5) {
                    child.add(f_conn);
                } else {
                    child.add(s_conn);
                }
                if (randomGenerator.nextDouble() < 0.5) {
                    child.get(child.size() - 1).setEnabled(true);
                }
                f_iterator++;
                s_iterator++;
            }
        }
        while (f_iterator < f_parent.size()) {
            child.add(f_parent.get(f_iterator));
            f_iterator++;
        }
        while (s_iterator < s_parent.size()) {
            child.add(s_parent.get(s_iterator));
            s_iterator++;
        }

        return createNeuralNetworkBasedOnConnections(child);
    }

    private NeuralNetwork createNeuralNetworkBasedOnConnections(List<Connection> connections) {
        if( connections.size() == 0)
            return new NeuralNetwork();

        int inputLayerSize = 0;
        int intermediateLayerSize = 0;
        NeuralNetwork net = new NeuralNetwork();
        net.setActivationFunction(connections.get(0).getFrom().getActivationFunction());

        for (Connection conn : connections) {
            Node from = conn.getFrom();
            if (net.getNodeById(from.getId()) == null) {
                net.addNode(from);
            }
            Node to = conn.getTo();
            if (net.getNodeById(to.getId()) == null) {
                net.addNode(to);
            }
        }

        for (Node node : net.get_nodes()) {
            if (LayerType.Input == node.getLayerType()
                    && inputLayerSize < node.getId()) {
                inputLayerSize = (int) node.getId();
            }
            if (LayerType.Intermediate == node.getLayerType()
                    && intermediateLayerSize < node.getId()) {
                intermediateLayerSize = (int) node.getId();
            }
        }
        net.setLayerSizes(inputLayerSize + 1, intermediateLayerSize + 1 - inputLayerSize);
        return net;
    }


    class ConnectionByIdComparator implements Comparator<Connection> {
        @Override
        public int compare(Connection a, Connection b) {
            double diff = a.getInnovationNumber() - b.getInnovationNumber();
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }
    }
}
