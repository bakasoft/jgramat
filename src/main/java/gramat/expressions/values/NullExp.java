package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class NullExp extends Expression {

    private Expression expression;

    public NullExp(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        if (expression.eval(context)) {
            context.sendValue(null);
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Create null value";
    }

}