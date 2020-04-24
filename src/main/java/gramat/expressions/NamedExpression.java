package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class NamedExpression extends Expression {

    private final String name;
    private final boolean soft;

    private Expression expression;

    public NamedExpression(Location location, String name, Expression expression, boolean soft) {
        super(location);
        this.name = Objects.requireNonNull(name);
        this.expression = Objects.requireNonNull(expression);
        this.soft = soft;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var softMode0 = context.softMode;

        if (soft) {
            context.softMode = true;
        }

        int pos0 = context.source.getPosition();
        if (expression.eval(context)) {
            int posF = context.source.getPosition();

            context.mark(pos0, posF, name);

            if (soft) {
                context.softMode = softMode0;
            }
            return true;
        }

        context.softMode = softMode0;
        return false;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);
        return this;
    }

    @Override
    public String getDescription() {
        return "Expression: " + name + (isSoft() ? " (soft)" : "");
    }

    public boolean isSoft() {
        return soft;
    }
}
