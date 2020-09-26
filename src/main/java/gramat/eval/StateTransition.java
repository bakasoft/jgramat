package gramat.eval;

public class StateTransition {

    public final State state;
    public final Transition transition;

    public StateTransition(State state, Transition transition) {
        this.state = state;
        this.transition = transition;
    }
}
