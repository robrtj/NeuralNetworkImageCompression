package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2015-01-04.
 */
public class InnovationNumber {
    private static InnovationNumber instance = null;
    private static long innovationNumber;

    protected InnovationNumber() {
        // Exists only to defeat instantiation.
    }

    public static InnovationNumber getInstance() {
        if (instance == null) {
            instance = new InnovationNumber();
        }
        return instance;
    }

    public long nextInnovationNumber(){
        return innovationNumber++;
    }
}
