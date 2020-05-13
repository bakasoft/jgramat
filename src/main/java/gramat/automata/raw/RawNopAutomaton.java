package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;


public class RawNopAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        var state = lang.state();
        return lang.automaton(state, state);
    }
}
