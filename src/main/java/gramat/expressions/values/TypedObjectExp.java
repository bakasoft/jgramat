package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.TypedObject;

import java.util.Objects;

public class TypedObjectExp extends Expression {

    private final Class<?> type;

    private Expression expression;

    public TypedObjectExp(Location location, Class<?> type, Expression expression) {
        super(location);
        this.type = Objects.requireNonNull(type);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.useObject(expression, type);
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
    public String getDescription() {
        return "Create typed object: " + type;
    }

}