package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Objects;

public class DebugExp extends Expression {

    private Expression expression;

    public DebugExp(Expression expression) {
        super(Objects.requireNonNull(expression).location);
        this.expression = expression;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        var location = source.getLocation();

        System.out.println(" ".repeat(context.debugTabs) + expression + " <- " + location);

        context.debugTabs++;

        boolean result = expression.eval(source, context);

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
