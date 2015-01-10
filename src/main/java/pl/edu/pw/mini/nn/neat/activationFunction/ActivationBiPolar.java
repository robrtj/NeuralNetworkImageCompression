package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationBiPolar extends ActivationFunction {
    private double threshold;

    public ActivationBiPolar() {
        this(0);
        bipolar = true;
    }

    @Override
    public double getValue() {
        return sum < threshold ? 0 : 1;
    }

    public ActivationBiPolar(double threshold) {
        super();
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
