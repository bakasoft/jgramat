package gramat.automata.raw;

import gramat.automata.State;

import java.util.Set;

public class RawLiteralAutomaton extends RawStringAutomaton {

    private final String value;

    public RawLiteralAutomaton(String value) {
        this.value = value;
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public State compile(State s0) {
        var sF = new State();

        compile(s0, sF);

        return sF;
    }

    @Override
    public void compile(State s0, State sF) {
        var array = value.toCharArray();
        var s = s0;

        for (var i = 0; i < array.length; i++) {
            var c = array[i];

            if (i == array.length - 1) {
                s.addTransition(sF, c);
            }
            else {
                s = s.makeTransition(c);
            }
        }

        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        if (value.isEmpty()) {
            return null;
        }
        return value.charAt(0);
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        if (value.length() <= 1) {
            return new RawNopAutomaton();
        }

        var newValue = value.substring(1);

        return new RawLiteralAutomaton(newValue);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
