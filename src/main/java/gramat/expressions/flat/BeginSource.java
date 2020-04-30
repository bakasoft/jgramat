package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.List;

public class BeginSource extends Expression {

    public BeginSource(Location location) {
        super(location);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return this;
    }

    @Override
    public boolean eval(EvalContext context) {
        return context.source.getPosition() == 0;
    }

    @Override
    public String getDescription() {
        return "Begin of source";
    }

}
