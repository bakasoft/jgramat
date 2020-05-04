package gramat.automata.raw;

import gramat.automata.State;

public class RawOptionalAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return null;
    }

    @Override
    public State compile(State s0) {
        return null;
    }

    @Override
    public void compile(State s0, State sF) {

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
