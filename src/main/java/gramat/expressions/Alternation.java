package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.flat.CharLiteral;
import gramat.expressions.flat.CharRange;
import gramat.expressions.flat.Literal;
import gramat.expressions.flat.Nop;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;

import java.util.List;
import java.util.Objects;


public class Alternation extends Expression {

    private final Expression[] expressions;

    public Alternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public boolean eval(EvalContext context) {
        int pos0 = context.source.getPosition();
        Expression lastEnter = null;
        boolean result = false;

        for (var expression : expressions) {
            if (context.enter(expression, pos0)) {
                lastEnter = expression;
            }
            else {
                continue;
            }

            if (expression.eval(context)) {
                result = true;
                break;
            }
            else {
                context.source.setPosition(pos0);
            }
        }

        if (lastEnter != null) {
            for (var expression : expressions) {
                context.remove(expression, pos0);

                if (lastEnter == expression) {
                    break;
                }
            }
        }

        return result;
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
            else if (areLiteral(expressions)) {
                return new LiteralAlternation(location, makeStrings(expressions));
            }

            optimizeAll(context, expressions);

            if (isCyclic()) {
                return this;
            }

            return new LinearAlternation(location, expressions);
        });
    }

    private boolean areLiteral(Expression[] expressions) {
        for (var expr : expressions) {
            if (!(expr instanceof Literal || expr instanceof CharLiteral)) {
                return false;
            }
        }

        return true;
    }

    private static String[] makeStrings(Expression[] expressions) {
        var result = new String[expressions.length];

        for (int i = 0; i < expressions.length; i++) {
            String value;
            var expr = expressions[i];

            if (expr instanceof Literal) {
                value = ((Literal)expr).getValue();
            }
            else if (expr instanceof CharLiteral) {
                value = String.valueOf(((CharLiteral)expr).getValue());
            }
            else {
                throw new ParseException("unsupported literal", expr.location);
            }

            result[i] = value;
        }

        return result;
    }

    @Override
    public String getDescription() {
        return "Alternation";
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
    }
}
