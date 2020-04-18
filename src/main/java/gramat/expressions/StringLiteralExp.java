package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class StringLiteralExp extends ValueExp {

    private final String value;

    public StringLiteralExp(Location location, String value) {
        super(location);
        this.value = value;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        throw source.error("not implemented");
    }

    @Override
    public Expression optimize() {
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
}
