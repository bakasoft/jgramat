package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;

import java.util.List;

public class RawLiteralAutomaton extends RawStringAutomaton {

    private final String value;

    public RawLiteralAutomaton(String value) {
        this.value = value;

        if (value.isEmpty()) {
            throw new RuntimeException();
        }
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
        var accepted = initial;

        for (var c : value.toCharArray()) {
            var state = context.language.state();
            var symbol = context.language.symbols.getChar(c);

            context.language.transition(accepted, state, symbol);

            accepted = state;
        }

        return context.segment(initial, accepted);
    }

    @Override
    public State build(Builder builder, State initial) {
        var chars = value.toCharArray();
        var last = initial;

        for (char c : chars) {
            var next = builder.newState();

            builder.newCharTransition(last, next, c);

            last = next;
        }

        return last;
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
