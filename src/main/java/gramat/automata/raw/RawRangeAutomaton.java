package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.SymbolRange;

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
    public NAutomaton build(Language lang) {
        var initial = lang.state();
        var accepted = lang.state();

        lang.transition(initial, accepted, new SymbolRange(begin, end));

        return lang.automaton(initial, accepted);
    }
}
