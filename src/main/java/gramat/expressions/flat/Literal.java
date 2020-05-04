package gramat.expressions.flat;

import gramat.automata.raw.RawAutomatable;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawLiteralAutomaton;
import gramat.compiling.Compiler;
import gramat.expressions.Expression;
import gramat.output.GrammarWriter;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;

import java.util.List;

public class Literal extends Expression implements RawAutomatable {

    private final String value;

    public Literal(Location location, String value) {
        super(location);
        this.value = value;
    }

    @Override
    public RawAutomaton makeAutomaton() {
        return new RawLiteralAutomaton(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return List.of();
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        if (value.isEmpty()) {
            return new Nop(location);
        }
        else if (value.length() == 1) {
            return new CharLiteral(location, value.charAt(0));
        }

        return this;
    }

    @Override
    public boolean eval(EvalContext context) {
        return context.source.pull(value);
    }

    @Override
    public String getDescription() {
        return "Literal: " + GramatWriter.toDelimitedString(value, '\"');
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "literal")) {
            writer.attribute("value", value);
            writer.close();
        }
    }
}
