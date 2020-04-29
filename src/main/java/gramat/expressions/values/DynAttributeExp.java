package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class DynAttributeExp extends Expression {

    private Expression nameExpression;
    private Expression valueExpression;

    public DynAttributeExp(Location location, Expression nameExpression, Expression valueExpression) {
        super(location);
        this.nameExpression = Objects.requireNonNull(nameExpression);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(nameExpression, valueExpression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();
        var name = nameExpression.captureString(context);

        if (name == null) {
            return false;
        }

        // TODO find real type

        if (valueExpression.eval(context)) {
            context.set(context.source.locationOf(pos0), name);
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            nameExpression = nameExpression.optimize(context);
            valueExpression = valueExpression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Set dynamic attribute.";
    }

}
