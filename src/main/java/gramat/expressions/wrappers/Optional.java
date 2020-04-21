package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class Optional extends Expression {

    private Expression expression;

    public Optional(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        expression.eval(context);
        return true;
    }

    @Override
    public Expression optimize() {
        // TODO
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
        expression = expression.debug();
        return new DebugExp(this);
    }


}
