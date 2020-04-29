package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.flat.Nop;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;


public class Alternation extends Expression {

    private final Expression[] expressions;

    public Alternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        int pos0 = context.source.getPosition();
        Expression lastEnter = null;
        boolean result = false;

        for (var expression : expressions) {
            if (context.enter(expression, pos0)) {
                lastEnter = expression;
            }
            else {
                continue;
            }

            if (expression.eval(context)) {
                result = true;
                break;
            }
            else {
                context.source.setPosition(pos0);
            }
        }

        if (lastEnter != null) {
            for (var expression : expressions) {
                context.remove(expression, pos0);

                if (lastEnter == expression) {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            if (expressions.length == 0) {
                return new Nop(location);
            }
            else if (expressions.length == 1) {
                return expressions[0].optimize(context);
            }

            optimizeAll(context, expressions);

            if (isCyclic()) {
                return this;
            }

            return new LinearAlternation(location, expressions);
        });
    }

    @Override
    public String getDescription() {
        return "Alternation";
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
    }
}
