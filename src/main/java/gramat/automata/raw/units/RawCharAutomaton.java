package gramat.automata.raw.units;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;

public class RawCharAutomaton extends RawStringAutomaton {

    public final int value;

    public RawCharAutomaton(int value) {
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
        if (value > 0) {
            return String.valueOf((char)value);
        }
        return "";
    }
}
