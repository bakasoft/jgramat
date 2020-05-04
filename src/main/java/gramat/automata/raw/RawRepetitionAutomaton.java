package gramat.automata.raw;

import gramat.automata.State;

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
    public State compile(State s0) {
        var sF = new State();
        compile(s0, sF);
        return sF;
    }

    @Override
    public void compile(State s0, State sF) {
        automaton.compile(s0, sF);
        automaton.compile(sF, sF);

        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        return null;
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        return this;
    }
}
