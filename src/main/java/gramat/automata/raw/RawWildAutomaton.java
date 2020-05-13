package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;

public class RawWildAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        var state = lang.state();

        lang.makeWild(state);

        return lang.automaton(state, state);
    }

}
