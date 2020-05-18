package gramat.automata.raw;

import gramat.eval.BeginSourceCheck;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;

public class RawSourceBegin extends RawAutomaton {
    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var state = lang.state();

            state.onEnter.add(new BeginSourceCheck());

            initialSet.add(state);
            acceptedSet.add(state);
        });
    }
}
