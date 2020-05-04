package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.expressions.values.DataExpr;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

abstract public class Expression {

    abstract public String getDescription();

    abstract public Expression _custom_optimize(Compiler context);

    abstract public List<Expression> getInnerExpressions();

    abstract public boolean eval(EvalContext context);

    public final String captureString(EvalContext context) {
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

    final public Expression optimize(Compiler context) {
        var result = _custom_optimize(context);

        if (!(result instanceof ValueTransaction) && result.hasValues()) {
            result = new ValueTransaction(location, result);
        }

        return result;
    }

    private static boolean _contains_data_expressions(List<Expression> expressions) {
        for (var inner : expressions) {
            if (inner instanceof DataExpr) {
                return true;
            }
        }
        return false;
    }

    protected final Location location;

    public Expression(Location location) {
        this.location = location;
    }

    final public Location getLocation() {
        return location;
    }

    public final boolean isCyclic() {
        return _is_cyclic(this, getInnerExpressions(), new Stack<>());
    }

    public final boolean hasValues() {
        return _has_values(this, new Stack<>());
    }

    private static boolean _has_values(Expression expr, Stack<Expression> stack) {
        if (expr instanceof DataExpr) {
            return true;
        }

        if (!stack.contains(expr)) {
            stack.push(expr);

            for (var inner : expr.getInnerExpressions()) {
                if (_has_values(inner, stack)) {
                    return true;
                }
            }

            stack.pop();
        }

        return false;
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

    abstract public void write(GrammarWriter writer);

}
