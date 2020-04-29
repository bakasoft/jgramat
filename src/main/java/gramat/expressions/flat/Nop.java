package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class Nop extends Expression {
    public Nop(Location location) {
        super(location);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression optimize(Compiler context) {
        return this;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        return true;
    }

    @Override
    public String getDescription() {
        return "No operation";
    }

}
