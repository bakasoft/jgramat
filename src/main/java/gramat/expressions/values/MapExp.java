package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EditSendFragment;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class MapExp extends DataExpr {

    private final String replacement;

    private Expression expression;

    public MapExp(Location location, String replacement, Expression expression) {
        super(location);
        this.replacement = replacement;
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
        if (expression.eval(context)) {
            context.add(new EditSendFragment(context.source.getLocation(), replacement));
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Map to: " + replacement;
    }

}