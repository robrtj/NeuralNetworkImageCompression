package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationBiPolar extends ActivationFunction{
    private double a;

    public ActivationBiPolar(){
        this(0);
    }

    @Override
    public double getValue() {
        return sum < a ? 0 : 1;
    }

    public ActivationBiPolar(double a){
        super();
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }
}
