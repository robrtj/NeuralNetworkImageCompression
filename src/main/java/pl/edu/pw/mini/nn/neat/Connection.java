package pl.edu.pw.mini.nn.neat;

import java.util.Comparator;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Connection implements  Cloneable{
    private int in;
    private int out;
    private double weight;
    private boolean enabled;
    private long innovationNumber;

    public Connection(int in, int out, double weight, boolean enabled, long innovationNumber) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.enabled = enabled;
        this.innovationNumber = innovationNumber;
    }

    public Connection(int id) {
        new Connection(0, 0, 0.0, false, id);
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

    public long getInnovationNumber() {
        return innovationNumber;
    }

    public void setInnovationNumber(long innovationNumber) {
        this.innovationNumber = innovationNumber;
    }

    @Override
    public String toString(){
        return "In " + in + "\n" +
                "Out " + out + "\n" +
                "Weight " + weight + "\n" +
                (enabled == true ? "Enabled" : "Disabled") + "\n" +
                "Ordinal number " + innovationNumber;
    }

    public Connection clone(){
        return new Connection(in, out, weight, enabled, innovationNumber);
    }

    public void disable() {
        setEnabled(false);
    }
}
