package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2014-12-14.
 */
public enum MutationType {
    AddConnection,
    AddNode,
    DeleteConnection,
    WeightMutation;

    private double threshold;

    static {
//        sum should count to 1.0!
        AddConnection.threshold = 0.4d;
        AddNode.threshold = 0.4d;
        DeleteConnection.threshold = 0.1d;
        WeightMutation.threshold = 0.1d;

//        interpolate set to sum=1
        interpolateThresholds();
    }

    private static void interpolateThresholds() {

    }

    public static MutationType getMutationType(double sample) throws Exception {
        if (sample < 0) {
            throw new Exception("Sample should be grater than 0!");
        }

        double threshold = 0.0d;
        for (MutationType type : MutationType.values()) {
            threshold += type.threshold;
            if (sample < threshold) {
                return type;
            }
        }
        return null;
    }
}
