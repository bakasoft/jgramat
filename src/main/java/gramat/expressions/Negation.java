package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Objects;

public class Negation extends Expression {

    private Expression expression;

    public Negation(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        var pos0 = source.getPosition();

        if (expression.eval(source, context)) {
            source.setPosition(pos0);
            return false;
        }

        source.moveNext();
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
