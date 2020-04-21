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
        var name = nameExpression.capture(context);  // TODO should this be wrapped?

        if (name == null) {
            return false;
        }

        // TODO find real type

        if (valueExpression.eval(context)) {
            context.set(name);
            return true;
        }

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
