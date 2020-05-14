package gramat.expressions.wrappers;

import gramat.automata.raw.RawOptionalAutomaton;
import gramat.automata.raw.RawRepetitionAutomaton;
import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.flat.CharAutomaton;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class Optional extends Expression {

    private Expression expression;

    public Optional(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        expression.eval(context);
        return true;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);

            if (expression instanceof CharAutomaton) {
                var amContent = ((CharAutomaton) expression).getAutomaton();

                return new CharAutomaton(expression.getLocation(), new RawOptionalAutomaton(amContent)).optimize(context);
            }

            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Optional";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "optional")) {
            writer.write(expression);
            writer.close();
        }
    }

}
