package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.compiling.ValueParser;
import gramat.expressions.Expression;
import gramat.runtime.EditSendSegment;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class ValueExp extends DataExpr {

    private final ValueParser parser;

    private Expression expression;

    public ValueExp(Location location, ValueParser parser, Expression expression) {
        super(location);
        this.parser = parser;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (expression.eval(context)) {
            var posF = context.source.getPosition();

            context.add(new EditSendSegment(context.source.locationOf(pos0), pos0, posF, parser));
            return true;
        }

        return false;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        if (parser != null) {
            return "Parse value: " + parser;
        }

        return "Capture value";
    }
}
