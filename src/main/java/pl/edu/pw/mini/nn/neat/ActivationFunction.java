package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2015-01-06.
 */
public interface ActivationFunction {
    //get value of f(x)
    double getValue(double x);

    //return activation value
    double compute(double[] input);
}
