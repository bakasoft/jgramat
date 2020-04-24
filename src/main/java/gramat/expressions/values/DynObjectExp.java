package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class DynObjectExp extends Expression {

    private Expression typeExp;

    private Expression expression;

    public DynObjectExp(Location location, Expression typeExp, Expression expression) {
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
            return context.useObject(expression, type);
        }

        return context.useObject(expression, typeName);
    }

    @Override
    public Expression optimize() {
        typeExp = typeExp.optimize();
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        typeExp = typeExp.link(context);
        expression = expression.link(context);
        return this;
    }

    @Override
    public String getDescription() {
        return "Create dynamic object";
    }

}