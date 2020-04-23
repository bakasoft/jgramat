package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.WildList;

import java.util.Objects;

public class DynAttributeExp extends Expression {

    private Expression nameExpression;
    private Expression valueExpression;

    public DynAttributeExp(Location location, Expression nameExpression, Expression valueExpression) {
        super(location);
        this.nameExpression = Objects.requireNonNull(nameExpression);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();
        var name = nameExpression.capture(context);

        if (name == null) {
            return false;
        }

        // TODO find real type

        if (valueExpression.eval(context)) {
            context.set(context.source.locationOf(pos0), name);
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public Expression optimize() {
        nameExpression = nameExpression.optimize();
        valueExpression = valueExpression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        nameExpression = nameExpression.link(context);
        valueExpression = valueExpression.link(context);
        return this;
    }

    @Override
    public DebugExp debug() {
        nameExpression = nameExpression.debug();
        valueExpression = valueExpression.debug();
        return new DebugExp(this);
    }

}
