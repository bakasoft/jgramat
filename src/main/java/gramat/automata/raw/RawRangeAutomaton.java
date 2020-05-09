package gramat.automata.raw;

import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;

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
    public NAutomaton build(NLanguage lang) {
        var s0 = lang.state();
        var sA = s0.linkRange(begin, end);
        // TODO implement not range
        return lang.automaton(s0, s0, sA);
    }
}
