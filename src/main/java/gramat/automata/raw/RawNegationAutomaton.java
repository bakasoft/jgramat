package gramat.automata.raw;

import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;

public class RawNegationAutomaton extends RawAutomaton {

    private final RawAutomaton automaton;

    public RawNegationAutomaton(RawAutomaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(NLanguage lang) {
        var am = automaton.build(lang);

        // Please notice the accept & reject states are swapped
        return lang.automaton(am.start, am.accept, am.reject);
    }
}
