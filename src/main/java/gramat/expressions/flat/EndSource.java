package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class EndSource extends Expression {

    public EndSource(Location location) {
        super(location);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return !context.source.alive();
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
        return "End of source";
    }

}
