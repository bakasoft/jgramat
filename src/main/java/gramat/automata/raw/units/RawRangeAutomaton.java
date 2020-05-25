package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
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
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        accepted.notEmpty(context);

        context.language.transitionRange(initial, accepted, begin, end);
    }
}
