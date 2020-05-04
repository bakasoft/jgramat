package gramat.expressions;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.raw.RawSeriesAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.flat.Nop;
import gramat.output.GrammarWriter;
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
                var series = new RawSeriesAutomaton();

                for (var automaton : automata) {
                    series.addAutomaton(automaton.makeAutomaton());
                }

                result.add(new CharAutomaton(location, series).optimize(context));
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

    public Expression[] getExpressions() {
        return expressions; // TODO don't return the real array
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
    }

    @Override
    public String getDescription() {
        return "Sequence";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "sequence")) {
            writer.write(expressions);
            writer.close();
        }
    }
}
