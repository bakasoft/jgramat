package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EditCloseValue;
import gramat.runtime.EditOpenWildObject;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class ObjectExp extends DataExpr {

    private final String typeHint;

    private Expression expression;

    public ObjectExp(Location location, String typeHint, Expression expression) {
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
        context.add(new EditOpenWildObject(context.source, context.source.getPosition(), typeHint));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source, context.source.getPosition()));
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Create object: " + typeHint;
    }

}