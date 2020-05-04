package gramat.automata.raw;

import gramat.automata.State;

import java.util.Set;

abstract public class RawAutomaton {

    abstract public RawAutomaton collapse();

    abstract public State compile(State s0);

    abstract public void compile(State s0, State sF);

    abstract public Character getSingleCharOrNull();

    abstract protected RawAutomaton removeFirstChar();

    public State compile() {
        var s0 = new State();

        compile(s0);

        return s0;
    }

}