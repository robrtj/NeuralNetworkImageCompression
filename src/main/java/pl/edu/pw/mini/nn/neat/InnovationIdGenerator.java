package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2015-01-04.
 */
public class InnovationIdGenerator {
    private static InnovationIdGenerator instance = null;
    private static long innovationNumber = 0;

    protected InnovationIdGenerator() {
        // Exists only to defeat instantiation.
    }

    public static InnovationIdGenerator getInstance() {
        if (instance == null) {
            instance = new InnovationIdGenerator();
        }
        return instance;
    }

    public long generate() {
        return innovationNumber++;
    }
}
