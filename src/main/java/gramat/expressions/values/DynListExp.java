package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.WildList;

import java.util.Objects;

public class DynListExp extends Expression {

    private Expression typeExp;
    private Expression expression;

    public DynListExp(Location location, Expression typeExp, Expression expression) {
        super(location);
        this.typeExp = Objects.requireNonNull(typeExp);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var typeName = typeExp.capture(context);

        if (typeName == null) {
            return false;
        }

        // TODO find real type

        return context.useList(expression, typeName);
    }

    @Override
    public Expression optimize() {
        typeExp = typeExp.optimize();
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);
        expression = expression.link(context);
        return this;
    }

    @Override
    public DebugExp debug() {
        expression = expression.debug();
        expression = expression.debug();
        return new DebugExp(this);
    }
}

