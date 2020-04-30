package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.flat.Nop;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Sequence extends Expression {

    private Expression[] expressions;

    public Sequence(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public boolean eval(EvalContext context) {
        int pos0 = context.source.getPosition();

        for (var expression : expressions) {
            if (!expression.eval(context)) {
                context.source.setPosition(pos0);
                return false;
            }
        }

        return true;
    }

    public Expression[] getExpressions() {
        return expressions; // TODO don't return the real array
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            if (expressions.length == 0) {
                return new Nop(location);
            }
            else if (expressions.length == 1) {
                return expressions[0].optimize(context);
            }

            optimizeAll(context, expressions);

            // collapse sub-sequences
            if (_contains_sequences(expressions)) {
                var collapsed = new ArrayList<Expression>();

                for (var expr : expressions) {
                    if (expr instanceof Sequence) {
                        var subSeq = (Sequence)expr;

                        collapsed.addAll(Arrays.asList(subSeq.expressions));
                    }
                    else {
                        collapsed.add(expr);
                    }
                }

                expressions = collapsed.toArray(Expression[]::new);
            }

            return this;
        });
    }

    private boolean _contains_sequences(Expression[] expressions) {
        for (var expr : expressions) {
            if (expr instanceof Sequence) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
    }

    @Override
    public String getDescription() {
        return "Sequence";
    }
}
