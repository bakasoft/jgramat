package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LinearAlternation extends Expression {

    private Expression[] expressions;

    public LinearAlternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public boolean eval(EvalContext context) {
        int pos0 = context.source.getPosition();

        for (var expression : expressions) {
            if (expression.eval(context)) {
                return true;
            } else {
                context.source.setPosition(pos0);
            }
        }

        return false;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            optimizeAll(context, expressions);

            // collapse sub-sequences
            if (_contains_alternation(expressions)) {
                var collapsed = new ArrayList<Expression>();

                for (var expr : expressions) {
                    if (expr instanceof LinearAlternation) {
                        var subAlt = (LinearAlternation)expr;

                        collapsed.addAll(Arrays.asList(subAlt.expressions));
                    }
                    else {
                        collapsed.add(expr);
                    }
                }

                expressions = collapsed.toArray(Expression[]::new);
            }

            return this;
        });
    }

    private boolean _contains_alternation(Expression[] expressions) {
        for (var expr : expressions) {
            if (expr instanceof LinearAlternation) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
    }

    @Override
    public String getDescription() {
        return "Linear-Alternation";
    }
}
