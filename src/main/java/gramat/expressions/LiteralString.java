package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.List;

public class LiteralString extends Expression {

    private final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public NState build(NCompiler compiler, NState initial) {
        var chars = value.toCharArray();
        var last = initial;

        for (char c : chars) {
            var next = compiler.lang.newState();
            var symbol = compiler.symbols.getChar(c);

            compiler.lang.newTransition(last, next, symbol);

            last = next;
        }

        return last;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
