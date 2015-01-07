package pl.edu.pw.mini.nn.neat.activationFunction;

public class ActivationUniPolar extends ActivationFunction{
    private double a;

    public ActivationUniPolar(){
        this(0);
    }

    @Override
    public double getValue() {
        return sum < a ? -1 : 1;
    }

    ActivationUniPolar(double a){
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }
}

