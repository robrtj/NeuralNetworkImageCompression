package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationUniPolar extends ActivationFunction {
    private double threshold;

    public ActivationUniPolar() {
        this(0);
    }

    @Override
    public double getValue() {
        return sum < threshold ? 0 : 1;
    }

    ActivationUniPolar(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
