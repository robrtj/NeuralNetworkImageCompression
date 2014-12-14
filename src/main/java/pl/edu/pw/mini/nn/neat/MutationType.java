package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2014-12-14.
 */
public enum MutationType {
    AddConnection,
    AddNode,
    DeleteConnection;

    private double threshold;
    static{
//        sum should count to 1.0!
        AddConnection.threshold = 0.4d;
        AddNode.threshold = 0.4d;
        DeleteConnection.threshold = 0.2d;
    }

    public static MutationType getMutationType(double sample){
        double threshold = AddConnection.threshold;
        if(sample < threshold){
            return AddConnection;
        }
        threshold += AddNode.threshold;
        if(sample < threshold) {
            return AddNode;
        }

//        don't need check last option
//        threshold += DeleteConnection.threshold;
//        if(sample < threshold) {
//            return DeleteConnection;
//        }
        return DeleteConnection;
    }
}
