package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

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
    public NState build(NCompiler compiler, NState initial) {
        // Non-recursive rules doesn't need special handling
        if (!expression.isRecursive()) {
            return expression.build(compiler, initial);
        }

        var fragment = compiler.makeFragment(this);
        var accepted = compiler.lang.newState();

        compiler.addRecursiveHook(() -> {
            if (!fragment.ready) {
                throw new RuntimeException("fragment not ready");
            }

            var id = compiler.counts.next(name);
            var push = compiler.checks.push(name + id);
            var pop = compiler.checks.pop(name + id);

            for (var target : fragment.targets) {
                var symbol = compiler.symbols.getCheck(target.symbol, push);
                var trn = compiler.lang.newTransition(initial, target.target, symbol);

                trn.actions.addAll(target.actions);
            }

            for (var source : fragment.sources) {
                var symbol = compiler.symbols.getCheck(source.symbol, pop);
                var trn = compiler.lang.newTransition(source.source, accepted, symbol);

                trn.actions.addAll(source.actions);
            }
        });

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of(expression);
    }
}
