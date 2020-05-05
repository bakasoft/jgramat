package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;

public class RawCharAutomaton extends RawStringAutomaton {

    public final char value;

    public RawCharAutomaton(char value) {
        this.value = value;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        var sF = builder.createState();
        var segment = builder.createSegment(s0, sF);
        builder.createTransition(s0, value, sF);
        sF.makeAccepted();
        return segment;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }
}
