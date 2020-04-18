package gramat.expressions;

import gramat.compiling.LinkContext;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;


public class CharLiteral extends Expression {

    private final char value;

    public CharLiteral(Location location, char value) {
        super(location);
        this.value = value;
    }

    @Override
    public boolean eval(Source source, EvalContext context) {
        var c = source.peek();

        if (c == null || c != value){
            return false;
        }

        source.moveNext();
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
    public DebugExp debug() {
        return new DebugExp(this);
    }

    @Override
    public String toString() {
        return "CharLiteral{" +
                "value=" + value +
                '}';
    }
}
