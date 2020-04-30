package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class Optional extends Expression {

    private Expression expression;

    public Optional(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        expression.eval(context);
        return true;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            // TODO
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Optional";
    }

}
