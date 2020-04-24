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
        var debugMode0 = context.debugMode;

        context.debugMode = true;

        boolean result = expression.eval(context);

        context.debugMode = debugMode0;

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
    public String getDescription() {
        return "Debug expression";
    }
}
