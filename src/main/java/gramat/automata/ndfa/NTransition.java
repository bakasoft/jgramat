package gramat.automata.ndfa;

import java.util.Objects;

public class NTransition {

    public final NState source;
    public final NState target;
    public final Symbol symbol;

    NTransition(NState source, NState target, Symbol symbol) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
    }

}
