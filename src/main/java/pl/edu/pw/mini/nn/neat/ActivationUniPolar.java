package pl.edu.pw.mini.nn.neat;

public class ActivationUniPolar implements ActivationFunction{
    private double a;

    public ActivationUniPolar(){
        this(0);
    }

    ActivationUniPolar(double a){
        this.a = a;
    }

    @Override
    public double compute(double input) {
        return input < a ? 0 : 1;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }
}
