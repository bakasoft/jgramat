package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.List;

public class Alternation extends Expression {

    private final List<Expression> items;

    public Alternation(List<Expression> items) {
        this.items = items;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();

        for (var item : items) {
            var last = item.build(compiler, initial);

            compiler.lang.newEmptyTransition(last, accepted);
        }

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return items;
    }
}
