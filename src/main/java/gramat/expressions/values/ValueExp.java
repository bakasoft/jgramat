package gramat.expressions.values;

import gramat.compiling.LinkContext;
import gramat.compiling.ValueParser;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.values.PlainValue;

import java.util.Objects;

public class ValueExp extends Expression {

    private final ValueParser parser;

    private Expression expression;

    public ValueExp(Location location, ValueParser parser, Expression expression) {
        super(location);
        this.parser = parser;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (expression.eval(context)) {
            var posF = context.source.getPosition();

            context.sendSegment(pos0, posF, parser);
            return true;
        }

        return false;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);
        return this;
    }

    @Override
    public String getDescription() {
        if (parser != null) {
            return "Parse value: " + parser;
        }

        return "Capture value";
    }
}
