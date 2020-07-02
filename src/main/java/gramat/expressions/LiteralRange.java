package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralRange extends Expression {

    private final char begin;
    private final char end;

    public LiteralRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = builder.newState();

        builder.newRangeTransition(initial, accepted, begin, end);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
