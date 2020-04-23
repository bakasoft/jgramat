package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class NamedExpression extends Expression {

    private final String name;

    private Expression expression;

    public NamedExpression(Location location, String name, Expression expression) {
        super(location);
        this.name = Objects.requireNonNull(name);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        int pos0 = context.source.getPosition();
        if (expression.eval(context)) {
            int posF = context.source.getPosition();

            context.mark(pos0, posF, name);
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
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
    public String toString() {
        return "named-expression(" + name + ")";
    }
}
