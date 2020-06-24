package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
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
    public NSegment build(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();
        var symbol = context.language.symbols.getChar(value);

        context.language.transition(initial, accepted, symbol);

        return context.segment(initial, accepted);
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
