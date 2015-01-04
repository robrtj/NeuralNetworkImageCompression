package pl.edu.pw.mini.nn.neat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pawel on 2015-01-04.
 */
public class MutationFactory {

    private Map<MutationType, Double> mutationTypes;

    public MutationFactory(){
        mutationTypes = new HashMap<>();
        initialization();
    }

    private void initialization() {
        double threshold = 0.0d;
        int count = MutationType.values().length;
        for (MutationType type : MutationType.values()) {
            mutationTypes.put(type, Double.valueOf(1 / count));
        }
    }

    private void normalizeThresholds() {
        double sum = 0;
        for(MutationType key : mutationTypes.keySet()){
            sum += mutationTypes.get(key);
        }
        for(MutationType key : mutationTypes.keySet()){
            double value = mutationTypes.get(key) /sum;
            mutationTypes.replace(key, value);
        }
    }

    //should be generic
    public void setThresholds(double addConnection, double addNode, double deleteConnection, double weightMutation){
        mutationTypes.replace(MutationType.AddConnection, addConnection);
        mutationTypes.replace(MutationType.AddNode, addNode);
        mutationTypes.replace(MutationType.DeleteConnection, deleteConnection);
        mutationTypes.replace(MutationType.WeightMutation, weightMutation);

        normalizeThresholds();
    }

    //TODO
    public void mutate(NeuralNetwork net) {

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
