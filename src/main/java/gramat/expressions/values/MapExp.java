package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class MapExp extends Expression {

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
    public Expression optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        if (expression.eval(context)) {
            context.sendFragment(replacement);
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Map to: " + replacement;
    }

}