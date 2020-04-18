package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class Sequence extends Expression {

    private final Expression[] expressions;

    public Sequence(Location location, Expression[] expressions) {
        super(location);
        this.expressions = expressions;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        var pos0 = source.getPosition();

        for (var expression : expressions) {
            if (!expression.eval(source, context)) {
                source.setPosition(pos0);
                return false;
            }
        }

        return true;
    }

    @Override
    public Expression optimize() {
        if (expressions.length == 0) {
            return new Nop(location);
        }
        else if (expressions.length == 1) {
            return expressions[0].optimize();
        }

        optimizeAll(expressions);

        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        linkAll(context, expressions);
        return this;
    }

    @Override
    public DebugExp debug() {
        debugAll(expressions);
        return new DebugExp(this);
    }
}
