package pl.edu.pw.mini.nn.neat;

import java.util.Comparator;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Connection implements  Cloneable{
    private Node in;
    private Node out;
    private double weight;
    private boolean enabled;
    private long innovationNumber;

    public Connection(Node in, Node out, double weight, boolean enabled) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.enabled = enabled;
    }

    public Connection(Node in, Node out, double weight, boolean enabled, long innovationNumber) {
        this(in, out, weight, enabled);
        this.innovationNumber = innovationNumber;
    }

    public Node getIn() {
        return in;
    }

    public void setIn(Node in) {
        this.in = in;
    }

    public Node getOut() {
        return out;
    }

    public void setOut(Node out) {
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

    public double getInId() {
        return in.getId();
    }

    public double getOutId() {
        return out.getId();
    }
}
