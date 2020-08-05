package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralString extends Expression {

    private final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var chars = value.toCharArray();
        var last = initial;

        for (char c : chars) {
            var next = builder.lang.newState();
            var symbol = builder.symbols.getChar(c);

            builder.lang.newTransition(last, next, symbol);

            last = next;
        }

        return last;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
