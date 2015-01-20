package pl.edu.pw.mini.nn.neat;

/**
 * Created by Pawel on 2014-12-04.
 */
public class Connection implements  Cloneable {
    private Node from;
    private Node to;
    private double weight;
    private boolean enabled;
    private long innovationNumber;

    public Connection(Node from, Node to, double weight, boolean enabled) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
        innovationNumber = -1;
    }

    public Connection(Node from, Node to, double weight, boolean enabled, long innovationNumber) {
        this(from, to, weight, enabled);
        this.innovationNumber = innovationNumber;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
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
    public String toString() {
        return "From " + from + "\n" +
                "To " + to + "\n" +
                "Weight " + weight + "\n" +
                (enabled ? "Enabled" : "Disabled") + "\n" +
                "Ordinal number " + innovationNumber;
    }

    @Override
    public Connection clone() {
        return new Connection(from, to, weight, enabled, innovationNumber);
    }

    public void disable() {
        setEnabled(false);
    }

    public double getFromId() {
        return from.getId();
    }

    public double getToId() {
        return to.getId();
    }
}
