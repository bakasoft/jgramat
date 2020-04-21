package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class Negation extends Expression {

    private Expression expression;

    public Negation(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (expression.eval(context)) {
            context.source.setPosition(pos0);
            return false;
        }

        if (context.source.alive()) {
            context.source.moveNext();
            return true;
        }

        context.source.setPosition(pos0);
        return false;
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
