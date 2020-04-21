package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.WildList;

import java.util.Objects;

public class ListExp extends Expression {

    private String typeName;
    private Expression expression;

    public ListExp(Location location, String typeName, Expression expression) {
        super(location);
        this.typeName = typeName;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.useList(expression, typeName);
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
    public DebugExp debug() {
        expression = expression.debug();
        return new DebugExp(this);
    }
}

