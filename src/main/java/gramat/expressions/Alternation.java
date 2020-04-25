package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.expressions.flat.Nop;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.Objects;


public class Alternation extends Expression {

    private final Expression[] expressions;

    public Alternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        int pos0 = context.source.getPosition();
        var added = new ArrayList<Expression>();

        for (var expression : expressions) {
            if (context.circuit.enter(expression, pos0)) {
                added.add(expression);
            }
            else {
                continue;
            }

            if (expression.eval(context)) {
                added.forEach(e -> context.circuit.remove(e, pos0));
                return true;
            }
            else {
                context.source.setPosition(pos0);
            }
        }

        added.forEach(e -> context.circuit.remove(e, pos0));
        return false;
    }

    @Override
    public Expression optimize() {
        if (expressions.length == 0) {
            return new Nop(location);
        }
        else if (expressions.length == 1) {
            return expressions[0].optimize();
        }

        optimizeAll(expressions);

        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        linkAll(context, expressions);
        return this;
    }

    @Override
    public String getDescription() {
        return "Alternation";
    }
}
