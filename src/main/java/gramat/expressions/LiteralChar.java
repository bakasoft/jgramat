package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralChar extends Expression {

    private final char value;

    public LiteralChar(char value) {
        this.value = value;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();
        var symbol = compiler.symbols.getChar(value);

        compiler.lang.newTransition(initial, accepted, symbol);

        return accepted;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
