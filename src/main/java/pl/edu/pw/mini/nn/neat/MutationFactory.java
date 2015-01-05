package pl.edu.pw.mini.nn.neat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactory {

    private Map<MutationType, Double> mutationTypes;
    private double ratio;

    public MutationFactory() {
        mutationTypes = new HashMap<>();
        ratio = 0.5d;

        mutationTypesInitialization();
    }

    private void mutationTypesInitialization() {
        double threshold = 0.0d;
        int count = MutationType.values().length;
        for (MutationType type : MutationType.values()) {
            mutationTypes.put(type, (double) (1 / count));
        }
    }

    private void normalizeThresholds() {
        double sum = 0;
        for (MutationType key : mutationTypes.keySet()) {
            sum += mutationTypes.get(key);
        }
        for (MutationType key : mutationTypes.keySet()) {
            double value = mutationTypes.get(key) / sum;
            mutationTypes.replace(key, value);
        }
    }

    //should be generic somehow
    public void setThresholds(double addConnection, double addNode, double deleteConnection, double weightMutation) {
        mutationTypes.replace(MutationType.AddConnection, addConnection);
        mutationTypes.replace(MutationType.AddNode, addNode);
        mutationTypes.replace(MutationType.DeleteConnection, deleteConnection);
        mutationTypes.replace(MutationType.WeightMutation, weightMutation);

        normalizeThresholds();
    }

    public boolean mutate(NeuralNetwork net) {
        Random random = new Random();
        double sample = random.nextDouble();
        boolean mutated = false;

        MutationType type = getMutationType(sample);
        switch (type) {
            case AddConnection:
                mutated = addConnection(net);
                break;
            case AddNode:
                mutated = addNode(net);
                break;
            case DeleteConnection:
                mutated = deleteConnection(net);
                break;
            case WeightMutation:
                mutated = weightMutation(net);
                break;
        }
        return mutated;
    }

    private boolean weightMutation(NeuralNetwork net) {
        Random random = new Random();
        int connectionCounter = net.get_connections().size();
        int sample = random.nextInt(connectionCounter);

        double weight = random.nextDouble();
        net.updateWeight(sample, weight);
        return true;
    }

    //TODO
    private boolean deleteConnection(NeuralNetwork net) {
        return false;
    }

    //TODO
    private boolean addNode(NeuralNetwork net) {
        return false;
    }

    //private
    public boolean addConnection(NeuralNetwork net) {
        Random rand = new Random();

        int in = rand.nextInt(net.get_nodes().size());
        int out = rand.nextInt(net.get_nodes().size());
        in = net.get_nodes().get(in).getId();
        out = net.get_nodes().get(out).getId();

        Connection connection = new Connection(in, out, rand.nextDouble(),
                true, -1);

        if(checkCorrectnessOfConnection(net, connection)){
            net.addConnection(connection);
            return true;
        }
        return false;
    }

    //private
    public boolean checkCorrectnessOfConnection(NeuralNetwork net, Connection connection) {
        if(connection.getIn() >= connection.getOut()){
            return false;
        }

        Node in = net.getNodeById(connection.getIn());
        Node out = net.getNodeById(connection.getOut());
        if( in.getLayerType() == LayerType.Output){
            return false;
        }
        if( out.getLayerType() == LayerType.Input){
            return false;
        }
        if( in.getLayerType() == LayerType.Intermediate
                && out.getLayerType() == LayerType.Intermediate ){
            return false;
        }

        return true;
    }

    private MutationType getMutationType(double sample) {
        double threshold = 0.0d;
        for (MutationType key : mutationTypes.keySet()) {
            threshold += mutationTypes.get(key);
            if (sample < threshold) {
                return key;
            }
        }

        return null;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
