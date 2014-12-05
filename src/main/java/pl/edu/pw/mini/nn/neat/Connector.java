package pl.edu.pw.mini.nn.neat;

import java.util.Comparator;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Connector implements Comparator<Connector>{
    private int in;
    private int out;
    private double weight;
    private boolean enabled;
    private int ordinalNumber;

    public Connector(int in, int out, double weight, boolean enabled, int ordinalNumber) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.enabled = enabled;
        this.ordinalNumber = ordinalNumber;
    }

    public Connector(int id) {
        new Connector(0, 0, 0.0, false, id);
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    @Override
    public String toString(){
        return "In " + in + "\n" +
                "Out " + out + "\n" +
                "Weight " + weight + "\n" +
                (enabled == true ? "Enabled" : "Disabled") + "\n" +
                "Ordinal number " + ordinalNumber;
    }

    @Override
    public int compare(Connector con1, Connector con2) {
        return 0;
    }
}
