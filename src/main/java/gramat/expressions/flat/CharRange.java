package gramat.expressions.flat;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawRangeAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

import java.util.List;

public class CharRange extends Expression implements RawAutomatable {

    private final char beginChar;
    private final char endChar;

    public CharRange(Location location, char begin, char end) {
        super(location);
        this.beginChar = begin;
        this.endChar = end;
    }

    @Override
    public RawAutomaton makeAutomaton() {
        return new RawRangeAutomaton(beginChar, endChar);
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        if (beginChar == endChar) {
            return new CharLiteral(location, beginChar);
        }
        return this;
    }

    @Override
    public boolean eval(EvalContext context) {
        var c = context.source.peek();

        if (c == null || c < beginChar || c > endChar) {
            return false;
        }

        context.source.moveNext();
        return true;
    }

    @Override
    public String getDescription() {
        return "Char Range: " + GramatWriter.toDelimitedString(String.valueOf(beginChar), '\'')
                + "-"  + GramatWriter.toDelimitedString(String.valueOf(endChar), '\'');
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-range")) {
            writer.attribute("begin", beginChar);
            writer.attribute("end", endChar);
            writer.close();
        }
    }
}
