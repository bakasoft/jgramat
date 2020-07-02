package gramat.automata.raw.units;

import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;

import java.util.List;

public class RawCharAutomaton extends RawStringAutomaton {

    public final int value;

    public RawCharAutomaton(int value) {
        this.value = value;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of();
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public State build(Builder builder, State initial) {
        var accepted = builder.newState();

        builder.newCharTransition(initial, accepted, (char)value);

        return accepted;
    }

    @Override
    public String getStringValue() {
        if (value > 0) {
            return String.valueOf((char)value);
        }
        return "";
    }
}
