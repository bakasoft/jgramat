package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class AttributeExp extends Expression {

    private final String name;
    private Expression valueExpression;

    public AttributeExp(Location location, String name, Expression valueExpression) {
        super(location);
        this.name = Objects.requireNonNull(name);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (valueExpression.eval(context)) {
            context.set(context.source.locationOf(pos0), name);
            return true;
        }

        return false;
    }

    @Override
    public Expression optimize() {
        valueExpression = valueExpression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        valueExpression = valueExpression.link(context);
        return this;
    }

    @Override
    public String getDescription() {
        return "Set attribute: " + name;
    }

}
