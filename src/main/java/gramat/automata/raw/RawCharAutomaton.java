package gramat.automata.raw;

import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;

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
    public NState build(NLanguage lang, NState start) {
        return start.linkChar(value);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }
}
