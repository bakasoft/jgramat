package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.WildObject;

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
        var typeName = typeExp.capture(context);  // TODO should this be wrapped?

        if (typeName == null) {
            return false;
        }

        // TODO find real type

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
    public DebugExp debug() {
        typeExp = expression.debug();
        expression = expression.debug();
        return new DebugExp(this);
    }

}