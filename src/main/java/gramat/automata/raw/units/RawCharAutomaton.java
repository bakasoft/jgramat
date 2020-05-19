package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
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
    public void build(NContext context) {
        var initial = context.initial();
        var accepted = context.accepted();

        context.transitionChar(initial, accepted, value);
    }

    @Override
    public String getStringValue() {
        if (value > 0) {
            return String.valueOf((char)value);
        }
        return "";
    }
}
