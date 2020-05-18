package gramat.automata.raw;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.util.parsing.Source;

public class RawSourceEnd extends RawAutomaton {
    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var initial = initialSet.create();
            var accepted = acceptedSet.create();

            lang.transition(initial, accepted, Source.EOF);
        });
    }
}
