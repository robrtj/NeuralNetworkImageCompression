package pl.edu.pw.mini.nn.neat.activationFunction;

/**
 * Created by Pawel on 2015-01-06.
 */
public abstract class ActivationFunction{
    double sum;
    boolean bipolar;

    protected ActivationFunction() {
        sum = 0;
        bipolar = false;
    }

    public void addInput(double input) {
        sum += input;
    }

    public abstract double getValue();

    public void reset() {
        sum = 0;
    }

    public double getSum() {
        return sum;
    }

    public boolean getType() {
        return bipolar;
    }

    public abstract ActivationFunction clone();
}
