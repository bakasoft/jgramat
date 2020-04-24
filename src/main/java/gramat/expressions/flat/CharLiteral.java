package gramat.expressions.flat;

import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;


public class CharLiteral extends Expression {

    private final char value;

    public CharLiteral(Location location, char value) {
        super(location);
        this.value = value;
    }

    @Override
    protected boolean evalImpl(EvalContext context) {
        var c = context.source.peek();

        if (c == null || c != value){
            return false;
        }

        context.source.moveNext();
        return true;
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
        return "Char: " + GramatWriter.toDelimitedString(String.valueOf(value), '\'');
    }
}
