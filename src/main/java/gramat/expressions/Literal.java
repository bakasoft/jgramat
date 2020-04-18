package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class Literal extends Expression {

    private final String value;

    public Literal(Location location, String value) {
        super(location);
        this.value = value;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        return source.pull(value);
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
    public DebugExp debug() {
        return new DebugExp(this);
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value='" + value + '\'' +
                '}';
    }
}
