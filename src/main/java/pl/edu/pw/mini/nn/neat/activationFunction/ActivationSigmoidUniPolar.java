package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationSigmoidUniPolar extends ActivationFunction {
    private double threshold;
    private float beta = 0.5f;

    public ActivationSigmoidUniPolar() {
        this(0);
    }

    @Override
    public double getValue() {
        return 1 / (1 + Math.exp(-beta*sum));
    }

    @Override
    public ActivationFunction clone() {
        ActivationSigmoidUniPolar function = new ActivationSigmoidUniPolar(threshold);
        function.addInput(sum);
        return function;
    }

    ActivationSigmoidUniPolar(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
