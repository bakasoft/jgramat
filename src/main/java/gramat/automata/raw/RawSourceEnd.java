package gramat.automata.raw;

import gramat.automata.actions.BeginSourceCheck;
import gramat.automata.actions.EndSourceCheck;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;

public class RawSourceEnd extends RawAutomaton {
    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        var state = lang.state();

        state.actions.add(new EndSourceCheck());

        return lang.automaton(state, state);
    }
}
