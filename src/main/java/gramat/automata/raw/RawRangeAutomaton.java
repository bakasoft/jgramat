package gramat.automata.raw;

import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;

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
    public NState build(NLanguage lang, NState start) {
        return start.linkRange(begin, end);
    }
}
