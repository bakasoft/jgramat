package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;

public class RawLiteralAutomaton extends RawStringAutomaton {

    private final String value;

    public RawLiteralAutomaton(String value) {
        this.value = value;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        var chs = value.toCharArray();
        var last = s0;

        for (var ch : chs) {
            var source = last;
            var target = builder.createState();

            builder.createTransition(source, ch, target);

            last = target;
        }

        last.makeAccepted();

        return builder.createSegment(s0, last);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
