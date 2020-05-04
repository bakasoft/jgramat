package gramat.expressions.values;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EditCloseValue;
import gramat.runtime.EditOpenTypedList;
import gramat.runtime.EditOpenTypedObject;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class TypedListExp extends DataExpr {

    private Class<?> type;
    private Expression expression;

    public TypedListExp(Location location, Class<?> type, Expression expression) {
        super(location);
        this.type = Objects.requireNonNull(type);
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
        context.add(new EditOpenTypedList(context.source, context.source.getPosition(), type));
        if (expression.eval(context)) {
            context.add(new EditCloseValue(context.source, context.source.getPosition()));
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Create typed list: " + type;
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "typed-list")) {
            writer.attribute("type", type);
            writer.write(expression);
            writer.close();
        }
    }
}

