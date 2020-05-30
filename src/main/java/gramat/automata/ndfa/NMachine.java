package gramat.automata.ndfa;

import java.util.*;

public class NMachine extends NSegment {

    public final List<NState> states;
    public final List<NTransition> transitions;

    public NMachine(NSegment segment, NGroup group) {
        super(segment.language, segment.initial, segment.accepted);
        this.states = Collections.unmodifiableList(group.states);
        this.transitions = Collections.unmodifiableList(group.transitions);
    }
}
