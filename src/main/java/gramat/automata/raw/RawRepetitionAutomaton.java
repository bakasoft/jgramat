package gramat.automata.raw;

import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;

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
    public NState build(NLanguage lang, NState start) {
        var end = automaton.build(lang, start);

        end.linkEmpty(start);

        return start;
    }

}
