package gramat.expressions.flat;

import gramat.compiling.Compiler;
import gramat.compiling.LinkContext;
import gramat.expressions.Expression;
import gramat.expressions.wrappers.DebugExp;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

import java.util.List;


public class CharLiteral extends Expression {

    private final char value;

    public CharLiteral(Location location, char value) {
        super(location);
        this.value = value;
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
        var c = context.source.peek();

        if (c == null || c != value){
            return false;
        }

        context.source.moveNext();
        return true;
    }

    @Override
    public String getDescription() {
        return "Char: " + GramatWriter.toDelimitedString(String.valueOf(value), '\'');
    }
}
