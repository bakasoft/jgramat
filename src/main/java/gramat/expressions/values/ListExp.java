package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class ListExp extends DataExpr {

    private String typeHint;
    private Expression expression;

    public ListExp(Location location, String typeHint, Expression expression) {
        super(location);
        this.typeHint = typeHint;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public boolean eval(EvalContext context) {
        return context.useList(expression, typeHint);
    }

    @Override
    public String getDescription() {
        return "Create list: " + typeHint;
    }
}

