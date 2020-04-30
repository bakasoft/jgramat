package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EditSet;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class AttributeExp extends DataExpr {

    private final String name;
    private Expression valueExpression;

    public AttributeExp(Location location, String name, Expression valueExpression) {
        super(location);
        this.name = Objects.requireNonNull(name);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(valueExpression);
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (valueExpression.eval(context)) {
            context.add(new EditSet(context.source, pos0, name));
            return true;
        }

        return false;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            valueExpression = valueExpression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Set attribute: " + name;
    }

}
