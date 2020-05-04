package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EditSendValue;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class NullExp extends DataExpr {

    private Expression expression;

    public NullExp(Location location, Expression expression) {
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
        if (expression.eval(context)) {
            context.add(new EditSendValue(context.source, context.source.getPosition(), null));
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Create null value";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "null")) {
            writer.write(expression);
            writer.close();
        }
    }

}