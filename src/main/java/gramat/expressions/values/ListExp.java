package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EditCloseValue;
import gramat.runtime.EditOpenWildList;
import gramat.runtime.EditOpenWildObject;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class ListExp extends DataExpr {

    private String typeHint;
    private Expression expression;

    public ListExp(Location location, String typeHint, Expression expression) {
        super(location);
        this.typeHint = typeHint;
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
        context.add(new EditOpenWildList(context.source, context.source.getPosition(), typeHint));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source, context.source.getPosition()));
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Create list: " + typeHint;
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "list")) {
            writer.attribute("type-hint", typeHint);
            writer.write(expression);
            writer.close();
        }
    }
}

