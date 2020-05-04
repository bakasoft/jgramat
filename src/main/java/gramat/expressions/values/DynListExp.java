package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.*;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class DynListExp extends DataExpr {

    private Expression typeExp;
    private Expression expression;

    public DynListExp(Location location, Expression typeExp, Expression expression) {
        super(location);
        this.typeExp = Objects.requireNonNull(typeExp);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(expression);
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            typeExp = typeExp.optimize(context);
            expression = expression.optimize(context);
            return this;
        });
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var typeName = typeExp.captureString(context);

        if (typeName == null) {
            return false;
        }

        var type = context.getType(typeName);

        if (type != null) {
            context.add(new EditOpenTypedList(context.source, context.source.getPosition(), type));
            if (expression.eval(context)) {
                context.add(new EditCloseValue(context.source, context.source.getPosition()));
                return true;
            }

            context.source.setPosition(pos0);
            return false;
        }

        context.add(new EditOpenWildList(context.source, context.source.getPosition(), typeName));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source, context.source.getPosition()));
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public String getDescription() {
        return "Create dynamic list";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "dynamic-list")) {
            writer.attribute("type", typeExp);
            writer.attribute("expression", expression);
            writer.close();
        }
    }
}

