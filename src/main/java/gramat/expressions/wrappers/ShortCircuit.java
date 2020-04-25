package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class ShortCircuit extends Expression {

    private Expression expression;

    public ShortCircuit(Location location, Expression expression) {
        super(location);
        this.expression = expression;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        if (context.pushCircuit(this)) {
            boolean result = expression.eval(context);

            context.popCircuit();

            return result;
        }

        return false;
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
        return "Short-circuit";
    }
}
