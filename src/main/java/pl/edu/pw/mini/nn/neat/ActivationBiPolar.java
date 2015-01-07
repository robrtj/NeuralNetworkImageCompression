package pl.edu.pw.mini.nn.neat;

public class ActivationBiPolar implements ActivationFunction{
    private double a;

    public ActivationBiPolar(){
        this(0);
    }

    ActivationBiPolar(double a){
        this.a = a;
    }

    @Override
    public double compute(double input) {
        return input < a ? -1 : 1;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }
}
