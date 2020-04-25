package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.expressions.flat.Nop;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class Sequence extends Expression {

    private final Expression[] expressions;

    public Sequence(Location location, Expression[] expressions) {
        super(location);
        this.expressions = expressions;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        int pos0 = context.source.getPosition();

        for (var expression : expressions) {
            if (!expression.eval(context)) {
                context.source.setPosition(pos0);
                return false;
            }
        }

        return true;
    }

    public Expression[] getExpressions() {
        return expressions; // TODO don't return the real array
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
        return "Sequence";
    }
}
