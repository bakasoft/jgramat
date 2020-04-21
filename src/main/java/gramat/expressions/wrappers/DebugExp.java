package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;

import java.util.Objects;

public class DebugExp extends Expression {

    private Expression expression;

    public DebugExp(Expression expression) {
        super(Objects.requireNonNull(expression).getLocation());
        this.expression = expression;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var location = context.source.getLocation();

        System.out.println(" ".repeat(context.debugTabs) + expression + " <- " + location);

        context.debugTabs++;

        boolean result = expression.eval(context);

        context.debugTabs--;

        System.out.println(" ".repeat(context.debugTabs) + expression + " = " + result + " <- " + location);

        return result;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);
        return this;
    }

    @Override
    public DebugExp debug() {
        return this;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
