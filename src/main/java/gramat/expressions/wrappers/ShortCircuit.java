package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.Sequence;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;


public class ShortCircuit extends Expression {

    private Expression expression;

    public ShortCircuit(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    public boolean eval(EvalContext context) {
        return evalImpl(context);
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
    protected boolean evalImpl(EvalContext context) {
        int pos = context.source.getPosition();

        if (context.circuit.enter(expression, pos)) {
            boolean result = expression.eval(context);
            context.circuit.remove(expression, pos);
            return result;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Last-Sequence";
    }
}
