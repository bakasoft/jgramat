package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;

public class RawLiteralAutomaton extends RawStringAutomaton {

    private final String value;

    public RawLiteralAutomaton(String value) {
        this.value = value;

        if (value.isEmpty()) {
            throw new RuntimeException();
        }
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context) {
        var last = context.initial();
        for (var c : value.toCharArray()) {
            var next = context.state();
            context.transitionChar(last, next, c);
            last = next;
        }
        context.accepted(last);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
