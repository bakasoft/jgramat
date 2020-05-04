package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class Repetition extends Expression {

    private Expression expression;
    private Expression separator;
    private final Integer maxCount;
    private final Integer minCount;

    public Repetition(Location location, Expression expression, Integer min, Integer max, Expression separator) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
        this.separator = separator;
        this.maxCount = max;
        this.minCount = min;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression, separator);
    }

    @Override
    public boolean eval(EvalContext context) {
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
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            // TODO
            expression = expression.optimize(context);

            if (separator != null) {
                separator = separator.optimize(context);
            }

            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Repetition";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "repetition")) {
            writer.attribute("expression", expression);
            writer.attribute("separator", separator);
            writer.attribute("max-count", maxCount);
            writer.attribute("min-count", minCount);
            writer.close();
        }
    }
}
