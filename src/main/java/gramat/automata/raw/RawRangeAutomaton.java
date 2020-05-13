package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.SymbolRange;

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
    public NAutomaton build(Language lang) {
        var initial = lang.state();
        var accepted = lang.state();

        lang.transition(initial, accepted, new SymbolRange(begin, end));

        return lang.automaton(initial, accepted);
    }
}
