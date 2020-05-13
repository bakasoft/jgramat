package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;


public class RawRepetitionAutomaton extends RawAutomaton {

    private final RawAutomaton automaton;

    public RawRepetitionAutomaton(RawAutomaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(automaton.collapse());
    }

    @Override
    public NAutomaton build(Language lang) {
        var state = lang.state();

        var am = automaton.build(lang);

        lang.transition(state, am.initial, null);

        lang.transition(am.accepts, state, null);

        return lang.automaton(state, state);
    }

}
