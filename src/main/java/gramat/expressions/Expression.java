package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.BooleanFunction;
import gramat.util.BooleanSupplier;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

abstract public class Expression {

    abstract public String getDescription();

    abstract public Expression optimize(Compiler context);

    abstract public List<Expression> getInnerExpressions();

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

    public boolean isCyclic() {
        return _is_cyclic(this, getInnerExpressions(), new Stack<>());
    }

    private static boolean _is_cyclic(Expression target, List<Expression> children, Stack<Expression> stack) {
        for (var expr : children) {
            if (expr == target) {
                return true;
            }
            else if (stack.contains(expr)) {
                continue;
            }

            stack.push(expr);

            if (_is_cyclic(target, expr.getInnerExpressions(), stack)) {
                return true;
            }

            stack.pop();
        }

        return false;
    }

    public static void optimizeAll(Compiler context, Expression[] expressions) {
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = expressions[i].optimize(context);
        }
    }

    protected static List<Expression> listOf(Expression... expressions) {
        var list = new ArrayList<Expression>();

        for (var expr : expressions) {
            if (expr != null) {
                list.add(expr);
            }
        }

        return list;
    }

}
