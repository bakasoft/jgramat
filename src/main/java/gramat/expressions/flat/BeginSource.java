package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

public class BeginSource extends Expression {

    public BeginSource(Location location) {
        super(location);
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return context.source.getPosition() == 0;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        return this;
    }

}
