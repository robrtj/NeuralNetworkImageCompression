package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationBiPolar extends ActivationFunction {
    private double threshold;

    public ActivationBiPolar() {
        this(0);
        bipolar = true;
    }

    @Override
    public double getValue() {
        return sum < threshold ? -1 : 1;
    }

    @Override
    public ActivationFunction clone() {
        ActivationBiPolar function = new ActivationBiPolar(threshold);
        function.addInput(sum);
        return function;
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
