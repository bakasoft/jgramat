package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class CyclicExpr extends Expression {

    private Expression expression;

    public CyclicExpr(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        int pos0 = context.source.getPosition();

        if (context.enterCycle(this, pos0)) {
            boolean result = expression.eval(context);
            context.exitCycle(this, pos0);
            return result;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Cyclic-Expression";
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "cyclic-control")) {
            writer.write(expression);
            writer.close();
        }
    }
}
