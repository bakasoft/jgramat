package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class LinearAlternation extends Expression {

    private final Expression[] expressions;

    public LinearAlternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
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
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            optimizeAll(context, expressions);
            return this;
        });
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
