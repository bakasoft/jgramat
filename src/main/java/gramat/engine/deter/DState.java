package gramat.engine.deter;

public class DState {

    public final int id;
    public final boolean accepted;

    public DTransition[] transitions;

    public DState(int id, boolean accepted) {
        this.id = id;
        this.accepted = accepted;
        this.transitions = null;
    }

}
