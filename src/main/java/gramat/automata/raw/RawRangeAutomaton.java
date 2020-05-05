package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;

public class RawRangeAutomaton extends RawAutomaton {

    public final char begin;
    public final char end;

    public RawRangeAutomaton(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        var sF = builder.createState();

        builder.createTransition(s0, begin, end, sF);

        sF.makeAccepted();

        return builder.createSegment(s0, sF);
    }
}
