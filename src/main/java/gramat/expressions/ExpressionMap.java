package gramat.expressions;

import gramat.framework.Component;
import gramat.framework.DefaultComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpressionMap extends DefaultComponent {

    private final Map<String, Expression> expressions;

    public ExpressionMap(Component parent) {
        super(parent);
        expressions = new HashMap<>();
    }

    public Expression findExpression(String name) {
        var rule = expressions.get(name);

        if (rule == null) {
            throw new RuntimeException();
        }

        return rule;
    }

    public void addExpression(String name, Expression expression) {
        expressions.put(name, expression);
    }

    public Set<String> getExpressionNames() {
        return expressions.keySet();
    }

}
