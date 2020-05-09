package gramat.automata.builder;

public class Segment {

    public final StateReference initial;
    public final StateReference rejected;
    public final StateReference accepted;

    public Segment(StateReference initial, StateReference rejected, StateReference accepted) {
        this.initial = initial;
        this.rejected = rejected;
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "{I=" + initial + ", R=" + rejected + ", A=" + accepted + '}';
    }
}
