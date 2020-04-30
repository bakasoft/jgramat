package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EditCloseValue;
import gramat.runtime.EditOpenTypedObject;
import gramat.runtime.EditOpenWildObject;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class TypedObjectExp extends DataExpr {

    private final Class<?> type;

    private Expression expression;

    public TypedObjectExp(Location location, Class<?> type, Expression expression) {
        super(location);
        this.type = Objects.requireNonNull(type);
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
        context.add(new EditOpenTypedObject(context.source.getLocation(), type));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source.getLocation()));
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Create typed object: " + type;
    }

}