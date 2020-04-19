package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

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
    public boolean eval(Source source, EvalContext context) {
        throw source.error("not implemented");
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
