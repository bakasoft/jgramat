package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class DebugExp extends Expression {

    private Expression expression;

    public DebugExp(Location location, Expression expression) {
        super(location);
        this.expression = expression;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var initialValue = context.debugMode;

        context.debugMode = true;

        boolean result = expression.eval(context);

        context.debugMode = initialValue;

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
}
