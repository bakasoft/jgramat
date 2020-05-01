package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.flat.CharLiteral;
import gramat.expressions.flat.Literal;
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

            expressions = collapse_literals(expressions);

            return this;
        });
    }

    private Expression[] collapse_literals(Expression[] expressions) {
        var result = new ArrayList<Expression>();

        for (int i = 0; i < expressions.length; i++) {
            var literals = new ArrayList<Expression>();

            for (int j = i; j < expressions.length; j++) {
                if (expressions[j] instanceof Literal || expressions[j] instanceof CharLiteral) {
                    literals.add(expressions[j]);
                    i = j;
                }
                else {
                    break;
                }
            }

            if (literals.size() >= 2) {
                StringBuilder literal = new StringBuilder();

                for (var expr : literals) {
                    if (expr instanceof Literal) {
                        literal.append(((Literal)expr).getValue());
                    }
                    else if (expr instanceof CharLiteral) {
                        literal.append(((CharLiteral)expr).getValue());
                    }
                    else {
                        throw new RuntimeException();
                    }
                }

                result.add(new Literal(literals.get(0).location, literal.toString()));
            }
            else {
                result.add(expressions[i]);
            }
        }

        return result.toArray(Expression[]::new);
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
