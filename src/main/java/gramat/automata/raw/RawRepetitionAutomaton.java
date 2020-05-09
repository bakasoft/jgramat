package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.Segment;
import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;

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
    public NAutomaton build(NLanguage lang) {
        var state = lang.state();
        var am = automaton.build(lang);

        state.linkEmpty(am.start);

        am.accept.linkEmpty(state);

        return lang.automaton(state, am.reject, state);
    }

}
