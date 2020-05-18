package gramat.automata.ndfa;

import gramat.eval.Action;

import java.util.Objects;

public class NActionPattern {

    public final Symbol symbol;
    public final NState source;
    public final NState target;
    public final Action action;

    public NActionPattern(NState source, Symbol symbol, NState target, Action action) {
        this.source = Objects.requireNonNull(source);
        this.symbol = Objects.requireNonNull(symbol);
        this.target = Objects.requireNonNull(target);
        this.action = Objects.requireNonNull(action);
    }

}
