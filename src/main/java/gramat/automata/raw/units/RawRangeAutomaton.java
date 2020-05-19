package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;

public class RawRangeAutomaton extends RawAutomaton {

    public final int begin;
    public final int end;

    public RawRangeAutomaton(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context) {
        var initial = context.initial();
        var accepted = context.accepted();

        context.transitionRange(initial, accepted, begin, end);
    }
}
