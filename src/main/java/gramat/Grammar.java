package gramat;

import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Source;

import java.util.Map;
import java.util.Set;

public class Grammar {

    private final Map<String, Expression> expressions;
    private final Map<String, Class<?>> types;

    public Grammar(Map<String, Expression> expressions, Map<String, Class<?>> types) {
        this.expressions = expressions;
        this.types = types;
    }

    public <T> T evalValue(String expressionName, Source source, Class<T> expectedType) {
        var result = evalValue(expressionName, source);

        if (!expectedType.isInstance(result)) {
            throw new GramatException("Unexpected type: " + result.getClass());
        }

        return expectedType.cast(result);
    }

    public Object evalValue(String expressionName, Source source) {
        var expression = getExpression(expressionName);

        if (expression == null) {
            throw new GramatException("Expression not found: " + expressionName);
        }

        var context = createEvalContext(source);

        if (!expression.eval(context)) {
            throw new GramatException("No value was generated.");
        }

        return context.getValue();
    }

    public EvalContext createEvalContext(Source source) {
        return new EvalContext(source, types);
    }

    public Set<String> getExpressionNames() {
        return expressions.keySet();
    }

    public Expression getExpression(String name) {
        return expressions.get(name);
    }

    public Set<String> getTypeNames() {
        return types.keySet();
    }

    public Class<?> getType(String name) {
        return types.get(name);
    }

}
