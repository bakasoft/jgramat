package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

import java.util.Objects;

public class DynObjectExp extends Expression {

    private Expression typeExp;

    private Expression expression;

    public DynObjectExp(Location location, Expression typeExp, Expression expression) {
        super(location);
        this.typeExp = Objects.requireNonNull(typeExp);
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        throw source.error("not implemented");
    }

    @Override
    public Expression optimize() {
        typeExp = typeExp.optimize();
        expression = expression.optimize();
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        typeExp = typeExp.link(context);
        expression = expression.link(context);
        return this;
    }

    @Override
    public DebugExp debug() {
        typeExp = expression.debug();
        expression = expression.debug();
        return new DebugExp(this);
    }

}