package gramat.automata.raw;

import gramat.automata.State;

import java.util.Set;

public class RawCharAutomaton extends RawStringAutomaton {

    public final char value;

    public RawCharAutomaton(char value) {
        this.value = value;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public State compile(State s0) {
        var sF = s0.makeTransition(value);
        sF.makeAccepted();
        return sF;
    }

    @Override
    public void compile(State s0, State sF) {
        s0.addTransition(sF, value);
        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        return value;
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        return new RawNopAutomaton();
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }
}
