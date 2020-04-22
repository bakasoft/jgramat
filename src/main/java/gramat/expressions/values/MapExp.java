package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class MapExp extends Expression {

    private final String replacement;

    private Expression expression;

    public MapExp(Location location, String replacement, Expression expression) {
        super(location);
        this.replacement = replacement;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        if (expression.eval(context)) {
            context.sendFragment(replacement);
            return true;
        }

        return false;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);

        if (replacement != null) {
            Class<?> type = context.getType(replacement);

            if (type != null) {
                return new TypedObjectExp(location, type, expression);
            }
        }

        return this;
    }

    @Override
    public DebugExp debug() {
        expression = expression.debug();
        return new DebugExp(this);
    }

}