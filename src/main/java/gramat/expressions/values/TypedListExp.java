package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class TypedListExp extends Expression {

    private Class<?> type;
    private Expression expression;

    public TypedListExp(Location location, Class<?> type, Expression expression) {
        super(location);
        this.type = Objects.requireNonNull(type);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.useList(expression, type);
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
        return "Create typed list: " + type;
    }
}

