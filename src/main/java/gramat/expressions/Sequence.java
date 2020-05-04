package gramat.expressions;

import gramat.automata.raw.RawSeriesAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.flat.CharAutomaton;
import gramat.expressions.flat.Nop;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.ArrayFilter;
import gramat.util.parsing.Location;

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
            var filter = new ArrayFilter<>(expressions);
            filter.map(item -> item.optimize(context));
            filter.unwrap(Sequence.class, seq -> seq.expressions);
            filter.ignore(Nop.class);
            filter.join(CharAutomaton.class, charAutos -> {
                var series = new RawSeriesAutomaton();

                for (var charAuto : charAutos) {
                    series.addAutomaton(charAuto.getAutomaton());
                }

                return new CharAutomaton(location, series).optimize(context);
            });

            if (filter.empty()) {
                return new Nop(location);
            }
            else if (filter.singleton()) {
                return filter.first();
            }

            expressions = filter.toArray(Expression[]::new);

            return this;
        });
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
