package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class DebugExp extends Expression {

    private Expression expression;

    public DebugExp(Location location, Expression expression) {
        super(location);
        this.expression = expression;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        var debugMode0 = context.debugMode;

        context.debugMode = true;

        boolean result = expression.eval(context);

        context.debugMode = debugMode0;

        return result;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Debug expression";
    }
}
