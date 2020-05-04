package gramat.expressions.wrappers;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class Negation extends Expression {

    private Expression expression;

    public Negation(Location location, Expression expression) {
        super(location);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();

        if (expression.eval(context)) {
            context.source.setPosition(pos0);
            return false;
        }

        if (context.source.alive()) {
            // TODO should move always
            context.source.moveNext();
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            // TODO
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Negation";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "negation")) {
            writer.write(expression);
            writer.close();
        }
    }

}
