package gramat.engine.deter;

public class DState {

    public final int id;
    public final boolean accepted;

    public final DTransitionList transitions;

    public DState(int id, boolean accepted) {
        this.id = id;
        this.accepted = accepted;
        this.transitions = new DTransitionList();
    }

}
