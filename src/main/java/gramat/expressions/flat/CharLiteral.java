package gramat.expressions.flat;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawCharAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

import java.util.List;


public class CharLiteral extends Expression implements RawAutomatable {

    private final char value;

    public CharLiteral(Location location, char value) {
        super(location);
        this.value = value;
    }

    @Override
    public RawAutomaton makeAutomaton() {
        return new RawCharAutomaton(this.value);
    }

    public char getValue() {
        return value;
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

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-literal")) {
            writer.attribute("value", value);
            writer.close();
        }
    }
}
