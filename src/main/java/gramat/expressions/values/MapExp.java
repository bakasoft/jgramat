package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EditSendFragment;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class MapExp extends DataExpr {

    private final String replacement;

    private Expression expression;

    public MapExp(Location location, String replacement, Expression expression) {
        super(location);
        this.replacement = replacement;
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public boolean eval(EvalContext context) {
        int pos0 = context.source.getPosition();
        if (expression.eval(context)) {
            context.add(new EditSendFragment(context.source, pos0, replacement));
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Map to: " + replacement;
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "map")) {
            writer.attribute("replacement", replacement);
            writer.write(expression);
            writer.close();
        }
    }

}