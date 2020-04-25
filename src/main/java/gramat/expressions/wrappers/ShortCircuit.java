package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.Sequence;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;


public class ShortCircuit extends Expression {

    private final Expression[] expressions;

    public ShortCircuit(Location location, Expression expression) {
        super(location);

        if (expression instanceof Sequence) {
            this.expressions = ((Sequence)expression).getExpressions();
        }
        else {
            throw new RuntimeException("XXX");
        }

    }

    public boolean eval(EvalContext context) {
        return evalImpl(context);
    }

    @Override
    public Expression optimize() {
        optimizeAll(expressions);
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        linkAll(context, expressions);
        return this;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        context.begin(this);
        int pos0 = context.source.getPosition();

        for (var expression : expressions) {
            if (!expression.eval(context)) {
                context.source.setPosition(pos0);
                context.rollback(this);
                return false;
            }
        }

        context.commit(this);
        return true;
    }

    @Override
    public String getDescription() {
        return "Last-Sequence";
    }
}
