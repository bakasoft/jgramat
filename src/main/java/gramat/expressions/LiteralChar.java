package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralChar extends Expression {

    private final char value;

    public LiteralChar(char value) {
        this.value = value;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = builder.lang.newState();
        var symbol = builder.symbols.getChar(value);

        builder.lang.newTransition(initial, accepted, symbol);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
