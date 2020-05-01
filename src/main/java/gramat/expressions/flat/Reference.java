package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.CyclicExpr;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;

import java.util.List;

public class Reference extends Expression {

    private final String name;

    public Reference(Location location, String name) {
        super(location);
        this.name = name;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public boolean eval(EvalContext context) {
        throw context.source.error("Expression not compiled: " + name);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        var expression = context.getExpression(name);

        if (expression == null) {
            throw new ParseException("expression not found: " + name, location);
        }

        if (expression.isCyclic()) {
            return new CyclicExpr(location, expression.optimize(context));
        }

        return expression.optimize(context);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Reference: " + name;
    }

}
