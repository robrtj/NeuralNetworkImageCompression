package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationSigmoidBiPolar extends ActivationFunction {
    private double threshold;
    private float beta = 0.5f;

    public ActivationSigmoidBiPolar() {
        this(0);
        bipolar = true;
    }

    @Override
    public double getValue() {
        return (1 - (Math.exp(-beta * sum)) / (1 + Math.exp(-beta * sum)));
    }

    @Override
    public ActivationFunction clone() {
        ActivationSigmoidBiPolar function = new ActivationSigmoidBiPolar(threshold);
        function.addInput(sum);
        return function;
    }

    public ActivationSigmoidBiPolar(double threshold) {
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
