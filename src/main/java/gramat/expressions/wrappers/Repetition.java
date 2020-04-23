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
        var hits = 0;
        var expect_more = false;

        while (expression.eval(context)) {
            if (separator != null){
                if (separator.eval(context)) {
                    expect_more = true;
                } else {
                    expect_more = false;
                    break;
                }
            }

            hits += 1;

            if (maxCount != null && hits >= maxCount) {
                break;
            }
        }

        if (expect_more || (minCount != null && hits < minCount)) {
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
    public String toString() {
        return "repetition(" + expression + ")";
    }
}
