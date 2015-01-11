package pl.edu.pw.mini.nn.neat;

import java.util.*;

/**
 * Created by Pawel on 2015-01-11.
 */
public class CrossoverFactory {
    Random randomGenerator = new Random();

    public NeuralNetwork cross(NeuralNetwork firstParent, NeuralNetwork secondParent) {
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
            if (f_conn.getInnovationNumber() < s_conn.getInnovationNumber()) {
                child.add(f_conn);
            } else if (f_conn.getInnovationNumber() > s_conn.getInnovationNumber()) {
                child.add(s_conn);
            } else {
                Connection newConn = f_conn.clone();
                if(randomGenerator.nextDouble() < 0.5){
                    newConn.setWeight(f_conn.getWeight());
                    newConn.setEnabled(f_conn.isEnabled());
                }else{
                    newConn.setWeight(s_conn.getWeight());
                    newConn.setEnabled(s_conn.isEnabled());
                }
                child.add(newConn);
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

        NeuralNetwork net = createNeuralNetworkBasedOnConnections(child);
        return net;
    }

    //TODO
    private NeuralNetwork createNeuralNetworkBasedOnConnections(List<Connection> connections) {
        return null;
    }


    class ConnectionByIdComparator implements Comparator<Connection> {
        @Override
        public int compare(Connection a, Connection b) {
            double diff = a.getInnovationNumber() - b.getInnovationNumber();
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }
    }
}
