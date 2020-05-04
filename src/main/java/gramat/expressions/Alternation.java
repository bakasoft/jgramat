package gramat.expressions;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.raw.RawParallelAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.flat.CharLiteral;
import gramat.expressions.flat.Literal;
import gramat.expressions.flat.Nop;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Alternation extends Expression {

    private Expression[] expressions;

    public Alternation(Location location, Expression[] expressions) {
        super(location);
        this.expressions = Objects.requireNonNull(expressions);
    }

    @Override
    public boolean eval(EvalContext context) {
        for (var expression : expressions) {
            if (expression.eval(context)) {
                return true;
            }
        }

        return false;
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

            // collapse sub-alternations
            if (_contains_alternation(expressions)) {
                var collapsed = new ArrayList<Expression>();

                for (var expr : expressions) {
                    if (expr instanceof Alternation) {
                        var subAlt = (Alternation)expr;

                        collapsed.addAll(Arrays.asList(subAlt.expressions));
                    }
                    else {
                        collapsed.add(expr);
                    }
                }

                expressions = collapsed.toArray(Expression[]::new);
            }

            expressions = collapseAutomatables(context, expressions);

            if (expressions.length == 1) {
                return expressions[0];
            }

            return this;
        });
    }

    public static Expression[] collapseAutomatables(Compiler context, Expression[] expressions) {
        var result = new ArrayList<Expression>();

        for (int i = 0; i < expressions.length; i++) {
            var automata = new ArrayList<RawAutomatable>();
            Location location = null;

            for (int j = i; j < expressions.length; j++) {
                var expr = expressions[j];

                if (expr instanceof RawAutomatable) {
                    if (location == null) {
                        location = expr.location;
                    }
                    automata.add((RawAutomatable)expr);
                    i = j;
                }
                else {
                    break;
                }
            }

            if (automata.size() >= 2) {
                var parallel = new RawParallelAutomaton();

                for (var automaton : automata) {
                    parallel.addAutomaton(automaton.makeAutomaton());
                }

                result.add(new CharAutomaton(location, parallel).optimize(context));
            }
            else {
                result.add(expressions[i]);
            }
        }

        return result.toArray(Expression[]::new);
    }

    private boolean _contains_alternation(Expression[] expressions) {
        for (var expr : expressions) {
            if (expr instanceof Alternation) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
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
        return "Linear-Alternation";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "alternation")) {
            writer.write(expressions);
            writer.close();
        }
    }
}
