package gramat.automata.dfa;

import gramat.util.AmWriter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DMachine {

    public final Set<DState> initial;
    public final Set<DState> accepted;
    public final Set<DState> states;
    public final Set<DTransition> transitions;

    public DMachine(Set<DState> initial, Set<DState> accepted, Set<DState> states, Set<DTransition> transitions) {
        this.initial = Collections.unmodifiableSet(initial);
        this.accepted = Collections.unmodifiableSet(accepted);
        this.states = Collections.unmodifiableSet(states);
        this.transitions = Collections.unmodifiableSet(transitions);
    }

    public String getAmCode() {
        return AmWriter.getAmCode(this);
    }
}
