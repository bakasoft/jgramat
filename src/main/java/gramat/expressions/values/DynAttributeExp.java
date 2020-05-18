package gramat.expressions.values;

import gramat.automata.raw.actuators.RawDynAttribute;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.expressions.flat.CharAutomaton;
import gramat.output.GrammarWriter;
import gramat.runtime.EditSet;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;
import java.util.Objects;

public class DynAttributeExp extends DataExpr {

    private Expression nameExpression;
    private Expression valueExpression;

    public DynAttributeExp(Location location, Expression nameExpression, Expression valueExpression) {
        super(location);
        this.nameExpression = Objects.requireNonNull(nameExpression);
        this.valueExpression = Objects.requireNonNull(valueExpression);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf(nameExpression, valueExpression);
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var name = nameExpression.captureString(context);

        if (name == null) {
            return false;
        }

        // TODO find real type

        if (valueExpression.eval(context)) {
            context.add(new EditSet(context.source, pos0, name));
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return context.recursiveTransform(this, () -> {
            nameExpression = nameExpression.optimize(context);
            valueExpression = valueExpression.optimize(context);

            if (nameExpression instanceof CharAutomaton && valueExpression instanceof CharAutomaton) {
                var amName = ((CharAutomaton)nameExpression).getAutomaton();
                var amValue = ((CharAutomaton)valueExpression).getAutomaton();
                return new CharAutomaton(location, new RawDynAttribute(amName, amValue));
            }

            return this;
        });
    }

    @Override
    public String getDescription() {
        return "Set dynamic attribute.";
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "dynamic-attribute")) {
            writer.attribute("name", nameExpression);
            writer.attribute("value", valueExpression);
            writer.close();
        }
    }

}
