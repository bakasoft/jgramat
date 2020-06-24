package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.Input;
import gramat.epsilon.State;
import gramat.util.parsing.Source;

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
    public NSegment build(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();
        var symbol = context.language.symbols.getChar(Source.EOF);

        context.language.transition(initial, accepted, symbol);

        return context.segment(initial, accepted);
    }

    @Override
    public State build(Builder builder, State initial) {
        var accepted = builder.newState();

        builder.newCharTransition(initial, accepted, Input.ETX);

        return accepted;
    }
}
