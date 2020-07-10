package gramat.engine.nodet;

import gramat.engine.Badge;
import gramat.engine.Symbol;

public class NTransition {

    public NState source;
    public NState target;
    public Symbol symbol;
    public Badge badge;

    public NTransition(NState source, NState target, Symbol symbol, Badge badge) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        this.badge = badge;
    }
}
