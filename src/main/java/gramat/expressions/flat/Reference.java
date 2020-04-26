package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.expressions.wrappers.ShortCircuit;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;

public class Reference extends Expression {

    private final String name;

    public Reference(Location location, String name) {
        super(location);
        this.name = name;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        throw context.source.error("Expression not compiled: " + name);
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public Expression link(LinkContext context) {
        var expression = context.getExpression(name);

        if (expression == null) {
            throw new ParseException("expression not found: " + name, location);
        }

        return expression;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Reference: " + name;
    }

}
