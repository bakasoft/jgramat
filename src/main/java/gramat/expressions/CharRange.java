package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class CharRange extends Expression {

    private final char beginChar;
    private final char endChar;

    public CharRange(Location location, char begin, char end) {
        super(location);
        this.beginChar = begin;
        this.endChar = end;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        var c = source.peek();

        if (c == null || c < beginChar || c > endChar) {
            return false;
        }

        source.moveNext();
        return true;
    }

    @Override
    public Expression optimize() {
        // TODO
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

    @Override
    public String toString() {
        return "CharRange{" +
                "beginChar=" + beginChar +
                ", endChar=" + endChar +
                '}';
    }
}
