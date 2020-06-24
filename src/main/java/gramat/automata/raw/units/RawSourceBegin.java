package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.Input;
import gramat.epsilon.State;

import java.util.List;

public class RawSourceBegin extends RawAutomaton {

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of();
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NSegment build(NContext context) {
        var state = context.language.state();

        // TODO

        return context.segment(state, state);
    }

    @Override
    public State build(Builder builder, State initial) {
        var accepted = builder.newState();

        builder.newCharTransition(initial, accepted, Input.STX);

        return accepted;
    }
}
