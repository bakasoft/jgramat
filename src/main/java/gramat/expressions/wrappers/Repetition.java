package gramat.expressions.wrappers;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.Objects;

public class Repetition extends Expression {

    private Expression expression;
    private Expression separator;
    private Integer maxCount;
    private Integer minCount;

    public Repetition(Location location, Expression expression, Integer min, Integer max, Expression separator) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
        this.separator = separator;
        this.maxCount = max;
        this.minCount = min;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var pos0 = context.source.getPosition();
        var count = 0;
        var expectMore = false;

        while (expression.eval(context)) {
            count++;

            if (separator != null){
                if (separator.eval(context)) {
                    expectMore = true;
                } else {
                    expectMore = false;
                    break;
                }
            }
            else {
                expectMore = false;
            }
        }

        if (expectMore) {
            context.source.setPosition(pos0);
            return false;
        }
        else if (minCount != null && count < minCount) {
            context.source.setPosition(pos0);
            return false;
        }
        else if (maxCount != null && count > maxCount) {
            context.source.setPosition(pos0);
            return false;
        }

        return true;
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();

        if (separator != null) {
            separator = separator.optimize();
        }

        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        expression = expression.link(context);

        if (separator != null) {
            separator = separator.link(context);
        }

        return this;
    }

    @Override
    public String getDescription() {
        return "Repetition";
    }
}
