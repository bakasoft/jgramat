package gramat.automata.raw;

import gramat.automata.State;

import java.util.Set;

public class RawNopAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public State compile(State s0) {
        s0.makeAccepted();
        return s0;
    }

    @Override
    public void compile(State s0, State sF) {
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
