package gramat.automata.raw;

import gramat.automata.builder.StateBuilder;
import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;

abstract public class RawAutomaton {

    abstract public RawAutomaton collapse();

    abstract public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0);

    public SegmentBuilder build() {
        var builder = new AutomatonBuilder();
        var s0 = builder.createState();

        return build(builder, s0);
    }

}