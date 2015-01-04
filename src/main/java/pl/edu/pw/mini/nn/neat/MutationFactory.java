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

    private InnovationNumber innovationNumberGenerator;

    public MutationFactory() {
        mutationTypes = new HashMap<>();
        ratio = 0.5d;
        innovationNumberGenerator = new InnovationNumber();

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

    public void mutate(NeuralNetwork net) {
        Random random = new Random();
        double sample = random.nextDouble();

        MutationType type = getMutationType(sample);
        switch (type) {
            case AddConnection:
                addConnection(net);
                break;
            case AddNode:
                addNode(net);
                break;
            case DeleteConnection:
                deleteConnection(net);
                break;
            case WeightMutation:
                weightMutation(net);
                break;
        }
    }

    private void weightMutation(NeuralNetwork net) {
        Random random = new Random();
        int connectionCounter = net.get_connections().size();
        int sample = random.nextInt(connectionCounter);

        double weight = random.nextDouble();
        net.updateWeight(sample, weight);
    }

    //TODO
    private void deleteConnection(NeuralNetwork net) {

    }

    //TODO
    private void addNode(NeuralNetwork net) {

    }

    //TODO
    private void addConnection(NeuralNetwork net) {
        Random rand = new Random();

        int in = rand.nextInt(net.get_nodes().size());
        int out = rand.nextInt(net.get_nodes().size());

        in = net.get_nodes().get(in).getId();
        out = net.get_nodes().get(out).getId();

        Connection connection = new Connection(in, out, rand.nextDouble(),
                true, innovationNumberGenerator.nextInnovationNumber());

        if(checkCorrectnessOfConnection(net, connection)){
            net.addConnection(connection);
        }
    }

    private boolean checkCorrectnessOfConnection(NeuralNetwork net, Connection connection) {
        if(connection.getIn() <= connection.getOut()){
            return false;
        }

        Node in = net.getNode(connection.getIn());
        Node out = net.getNode(connection.getOut());
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
}
