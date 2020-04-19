package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Objects;

public class AttributeExp extends Expression {

    private String name;
    private Expression valueExpression;

    public AttributeExp(Location location, String name, Expression valueExpression) {
        super(location);
        this.name = Objects.requireNonNull(name);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        throw source.error("not implemented");
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
    public DebugExp debug() {
        valueExpression = valueExpression.debug();
        return new DebugExp(this);
    }

}
