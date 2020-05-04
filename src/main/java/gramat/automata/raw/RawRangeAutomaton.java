package gramat.automata.raw;

import gramat.automata.State;

import java.util.HashSet;
import java.util.Set;

public class RawRangeAutomaton extends RawAutomaton {

    public final char begin;
    public final char end;

    public RawRangeAutomaton(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public State compile(State s0) {
        var sF = s0.makeTransition(begin, end);
        sF.makeAccepted();
        return sF;
    }

    @Override
    public void compile(State s0, State sF) {
        s0.addTransition(sF, begin, end);
        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        if (begin == end) {
            return begin;
        }
        return null;
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        throw new RuntimeException("Cannot remove first char");
    }
}
