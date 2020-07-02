package gramat.automata.raw.units;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.Input;
import gramat.epsilon.State;

import java.util.List;

public class RawSourceEnd extends RawAutomaton {

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

        builder.newCharTransition(initial, accepted, Input.ETX);

        return accepted;
    }
}
