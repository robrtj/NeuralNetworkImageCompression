package pl.edu.pw.mini.nn.neat;

import java.util.Comparator;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Connection implements  Cloneable{
    private double in;
    private double out;
    private double weight;
    private boolean enabled;
    private long innovationNumber;

    public Connection(double in, double out, double weight, boolean enabled) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.enabled = enabled;
    }

    public Connection(double in, double out, double weight, boolean enabled, long innovationNumber) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.enabled = enabled;
        this.innovationNumber = innovationNumber;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public double getWeight() {
        return weight;
    }

    //TODO
    //waga jest interpolowana do przedzialu dziedziny f. aktywacji
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
        Connection conn = new Connection(in, out, weight, enabled);
        conn.setInnovationNumber(innovationNumber);
        return conn;
    }

    public void disable() {
        setEnabled(false);
    }
}
