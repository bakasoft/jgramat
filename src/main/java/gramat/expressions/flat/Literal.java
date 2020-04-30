package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

import java.util.List;

public class Literal extends Expression {

    private final String value;

    public Literal(Location location, String value) {
        super(location);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        if (value.isEmpty()) {
            return new Nop(location);
        }
        else if (value.length() == 1) {
            return new CharLiteral(location, value.charAt(0));
        }

        return this;
    }

    @Override
    public boolean eval(EvalContext context) {
        return context.source.pull(value);
    }

    @Override
    public String getDescription() {
        return "Literal: " + GramatWriter.toDelimitedString(value, '\"');
    }
}
