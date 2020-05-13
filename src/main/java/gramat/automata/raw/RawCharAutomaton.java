package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;

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
    public NAutomaton build(Language lang) {
        var initial = lang.state();
        var accepted = lang.state();

        lang.transition(initial, accepted, value);

        return lang.automaton(initial, accepted);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }
}
