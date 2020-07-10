package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class Alternation extends Expression {

    private final List<Expression> items;

    public Alternation(List<Expression> items) {
        this.items = items;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var accepted = builder.root.newState();

        for (var item : items) {
            var last = item.build(builder, initial);

            builder.root.newEmptyTransition(last, accepted);
        }

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return items;
    }
}
