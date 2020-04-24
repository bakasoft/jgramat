package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

public class CharRange extends Expression {

    private final char beginChar;
    private final char endChar;

    public CharRange(Location location, char begin, char end) {
        super(location);
        this.beginChar = begin;
        this.endChar = end;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var c = context.source.peek();

        if (c == null || c < beginChar || c > endChar) {
            return false;
        }

        context.source.moveNext();
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
    public String getDescription() {
        return "Char Range: " + GramatWriter.toDelimitedString(String.valueOf(beginChar), '\'')
                + "-"  + GramatWriter.toDelimitedString(String.valueOf(endChar), '\'');
    }
}
