package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;


public class ShortCircuit extends Expression {

    private Expression expression;

    public ShortCircuit(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    public boolean eval(EvalContext context) {
        return evalImpl(context);
    }

    @Override
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        int pos = context.source.getPosition();

        if (context.enter(expression, pos)) {
            boolean result = expression.eval(context);
            context.remove(expression, pos);
            return result;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Last-Sequence";
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }
}
