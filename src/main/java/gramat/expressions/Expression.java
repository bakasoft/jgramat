package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

abstract public class Expression {

    abstract public Expression optimize();
    abstract public Expression link(LinkContext context);
    abstract public DebugExp debug();

    abstract public boolean eval(Source source, EvalContext context);

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

    public static void debugAll(Expression[] expressions) {
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = expressions[i].debug();
        }
    }
}
