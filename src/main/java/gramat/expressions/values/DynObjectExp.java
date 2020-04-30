package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.*;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class DynObjectExp extends DataExpr {

    private Expression typeExp;
    private Expression expression;

    public DynObjectExp(Location location, Expression typeExp, Expression expression) {
        super(location);
        this.typeExp = Objects.requireNonNull(typeExp);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            typeExp = typeExp.optimize(context);
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var typeName = typeExp.captureString(context);

        if (typeName == null) {
            return false;
        }

        var type = context.getType(typeName);

        if (type != null) {
            context.add(new EditOpenTypedObject(context.source.getLocation(), type));
            if (expression.eval(context)) {
                context.add(new EditCloseValue(context.source.getLocation()));
                return true;
            }

            context.source.setPosition(pos0);
            return false;
        }

        context.add(new EditOpenWildObject(context.source.getLocation(), typeName));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source.getLocation()));
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public String getDescription() {
        return "Create dynamic object";
    }

}