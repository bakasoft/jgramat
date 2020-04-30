package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EditCloseValue;
import gramat.runtime.EditOpenJoin;
import gramat.runtime.EditOpenWildList;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class JoinExp  extends DataExpr {

    private Expression expression;

    public JoinExp(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public boolean eval(EvalContext context) {
        context.add(new EditOpenJoin(context.source.getLocation()));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source.getLocation()));
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Join strings";
    }

}