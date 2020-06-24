package gramat.epsilon;

import gramat.util.AmWriter;

import java.util.List;
import java.util.Set;

public class Machine {

    public final State initial;
    public final State accepted;
    public final Set<State> states;
    public final List<Transition> transitions;

    public Machine(State initial, State accepted, Set<State> states, List<Transition> transitions) {
        this.initial = initial;
        this.accepted = accepted;
        this.states = states;
        this.transitions = transitions;
    }

    public String getAmCode() {
        return AmWriter.getAmCode(this);
    }
}
