package gramat.automata.raw;

import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;

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
    public NAutomaton build(NLanguage lang) {
        var s0 = lang.state();
        var sA = s0.linkChar(value);
        var sR = lang.state();

        s0.linkNot(value, sR);

        return lang.automaton(s0, sR, sA);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }
}
