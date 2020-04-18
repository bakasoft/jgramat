package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Objects;

public class ObjectExp extends ValueExp {

    private final String typeName;

    private Expression expression;

    public ObjectExp(Location location, String typeName, Expression expression) {
        super(location);
        this.typeName = Objects.requireNonNull(typeName);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        throw source.error("not implemented");
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);

        Class<?> type = context.getType(typeName);

        if (type != null) {
            return new TypedObjectExp(location, type, expression);
        }

        return this;
    }

    @Override
    public DebugExp debug() {
        expression = expression.debug();
        return new DebugExp(this);
    }

}