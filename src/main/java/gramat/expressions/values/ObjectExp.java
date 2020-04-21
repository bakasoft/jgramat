package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.WildObject;

import java.util.Objects;

public class ObjectExp extends Expression {

    private final String typeName;

    private Expression expression;

    public ObjectExp(Location location, String typeName, Expression expression) {
        super(location);
        this.typeName = typeName;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.useObject(expression, typeName);
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);

        if (typeName != null) {
            Class<?> type = context.getType(typeName);

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