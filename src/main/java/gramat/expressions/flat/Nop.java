package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class Nop extends Expression {
    public Nop(Location location) {
        super(location);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        throw context.source.error("not implemented");
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
    public String getDescription() {
        return "No operation";
    }

}
