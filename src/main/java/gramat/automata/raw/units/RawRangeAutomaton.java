package gramat.automata.raw.units;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.SymbolRange;
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
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var initial = initialSet.create();
            var accepted = acceptedSet.create();

            lang.transition(initial, accepted, new SymbolRange(begin, end));
        });
    }
}
