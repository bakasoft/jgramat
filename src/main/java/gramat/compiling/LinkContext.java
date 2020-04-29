package gramat.compiling;

import gramat.expressions.Expression;
import gramat.expressions.NamedExpression;

import java.util.Stack;
import java.util.function.Supplier;

abstract public class LinkContext {

    abstract public NamedExpression getExpression(String name);

    private final Stack<Expression> recursiveStack = new Stack<>();

    public Expression recursiveTransform(Expression expression, Supplier<Expression> supplier) {
        if (recursiveStack.contains(expression)) {
            return expression;
        }

        recursiveStack.push(expression);

        var result = supplier.get();

        recursiveStack.pop();

        return result;
    }

}
