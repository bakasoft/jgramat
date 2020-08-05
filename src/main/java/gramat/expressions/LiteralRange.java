package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralRange extends Expression {

    private final char begin;
    private final char end;

    public LiteralRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();
        var symbol = compiler.symbols.getRange(begin, end);

        compiler.lang.newTransition(initial, accepted, symbol);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
