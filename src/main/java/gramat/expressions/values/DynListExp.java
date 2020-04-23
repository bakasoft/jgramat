package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

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
        var typeName = typeExp.captureString(context);

        if (typeName == null) {
            return false;
        }

        var type = context.getType(typeName);

        if (type != null) {
            return context.useList(expression, type);
        }

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
        return this;
    }
}

