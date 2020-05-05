package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;

public class RawNopAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        return builder.createSegment(s0, s0);
    }
}
