package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class Sequence extends Expression {

    private final List<Expression> items;

    public Sequence(List<Expression> items) {
        this.items = items;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        var last = initial;

        for (var item : items) {
            last = item.build(builder, last);
        }

        return last;
    }

    @Override
    public List<Expression> getChildren() {
        return items;
    }
}
