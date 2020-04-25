package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.Circuit;
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
        boolean result;
        int pos = context.source.getPosition();

        if (context.circuit.enter(this, pos)) {
            System.out.println("GRANTED!");
            result = expression.eval(context);
        }
        else {
            System.out.println("DENIED!");
            result = false;
        }

        context.circuit.remove(this, pos);

        return result;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return expression; /////////////////////////////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< this
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
