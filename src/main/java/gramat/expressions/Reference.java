package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

public class Reference extends Expression {

    private final String name;

    public Reference(Location location, String name) {
        super(location);
        this.name = name;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        throw source.error("Expression not compiled: " + name);
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
    public DebugExp debug() {
        return new DebugExp(this);
    }
}
