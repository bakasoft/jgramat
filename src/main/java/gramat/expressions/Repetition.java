package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

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
    public boolean eval(Source source, EvalContext context) {
        var pos0 = source.getPosition();
        var hits = 0;
        var expect_more = false;

        while (expression.eval(source, context)) {
            if (separator != null){
                if (separator.eval(source, context)) {
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
            source.setPosition(pos0);
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
    public DebugExp debug() {
        expression = expression.debug();

        if (separator != null) {
            separator = separator.debug();
        }

        return new DebugExp(this);
    }

    @Override
    public String toString() {
        return "repetition(" + expression + ")";
    }
}
