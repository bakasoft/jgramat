package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class Literal extends Expression {

    private final String value;

    public Literal(Location location, String value) {
        super(location);
        this.value = value;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.source.pull(value);
    }

    @Override
    public Expression optimize() {
        // TODO
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        return this;
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value='" + value + '\'' +
                '}';
    }
}
