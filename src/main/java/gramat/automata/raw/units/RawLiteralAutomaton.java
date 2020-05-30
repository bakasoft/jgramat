package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;

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
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var last = initial;
        var symbols = value.toCharArray();

        accepted.notEmpty(context);

        for (var i = 0; i < symbols.length; i++) {
            boolean isLast = (i == symbols.length - 1);

            NStateSet next;

            if (isLast) {
                next = accepted;
            }
            else {
                next = NStateSet.of(context.language.state());
            }

            var symbol = context.language.symbols.getChar(symbols[i]);
            context.language.transition(last, next, symbol);

            last = next;
        }
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
