package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NTool;
import gramat.engine.symbols.Symbol;

import java.util.HashSet;
import java.util.List;

public class Rule extends Expression {

    private static int idCount = 0;

    public final String name;
    public final Expression expression;

    public Rule(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
        // Non-recursive rules doesn't need special handling
        if (!expression.isRecursive()) {
            return expression.build(builder, initial);
        }

        var fragment = builder.makeFragment(this);
        var accepted = builder.root.newState();

        builder.addRecursiveHook(() -> {
            if (!fragment.ready) {
                throw new RuntimeException("fragment not ready");
            }

            var id = builder.nextCount(name);
            var push = builder.root.checks.push(name + id);
            var pop = builder.root.checks.pop(name + id);

            for (var target : fragment.targets) {
                builder.root.newTransition(initial, target.target, target.symbol, push);
            }

            for (var source : fragment.sources) {
                builder.root.newTransition(source.source, accepted, source.symbol, pop);
            }
        });

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(expression);
    }
}
