package pl.edu.pw.mini.nn.neat.activationFunction;

/**
 * Created by Pawel on 2015-01-06.
 */
public abstract class ActivationFunction {
    double sum;

    protected ActivationFunction(){
        sum =0;
    }

    public void addInput(double input){
        sum += input;
    };

    public abstract double getValue();
}

