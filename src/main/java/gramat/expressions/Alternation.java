package gramat.expressions;

import gramat.automata.raw.RawParallelAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.flat.CharAutomaton;
import gramat.expressions.flat.Nop;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.ArrayFilter;
import gramat.util.parsing.Location;

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
            var filter = new ArrayFilter<>(expressions);
            filter.map(item -> item.optimize(context));
            filter.unwrap(Alternation.class, alt -> alt.expressions);
            filter.ignore(Nop.class);
            filter.join(CharAutomaton.class, charAutos -> {
                var parallel = new RawParallelAutomaton();

                for (var charAuto : charAutos) {
                    parallel.addAutomaton(charAuto.getAutomaton());
                }

                return new CharAutomaton(location, parallel).optimize(context);
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

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expressions);
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
