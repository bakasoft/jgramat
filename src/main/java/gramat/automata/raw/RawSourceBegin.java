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
        var state = lang.state();

        state.actions.add(new BeginSourceCheck());

        return lang.automaton(state, state);
    }
}
