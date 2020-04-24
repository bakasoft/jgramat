package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

abstract public class Expression {

    abstract public String getDescription();

    abstract public Expression optimize();
    abstract public Expression link(LinkContext context);

    abstract protected boolean evalImpl(EvalContext context);

    public boolean eval(EvalContext context) {
        context.begin(this);

        if (evalImpl(context)) {
            context.commit(this);
            return true;
        }
        else {
            context.rollback(this);
            return false;
        }
    }

    public String captureString(EvalContext context) {
        var subContext = context.createEmptyContext();

        if (eval(subContext)) {
            var result = subContext.getValue();

            if (result instanceof String) {
                return (String)result;
            }
            else {
                throw context.source.error("Expected to capture a string.");
            }
        }

        return null;
    }

    protected final Location location;

    public Expression(Location location) {
        this.location = location;
    }

    final public Location getLocation() {
        return location;
    }

    public static void optimizeAll(Expression[] expressions) {
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = expressions[i].optimize();
        }
    }

    public static void linkAll(LinkContext context, Expression[] expressions) {
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = expressions[i].link(context);
        }
    }

}
