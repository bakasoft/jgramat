package gramat.engine.nodet;

import gramat.engine.ActionList;
import gramat.engine.Badge;
import gramat.engine.Symbol;

public class NTransition {

    public final NState source;
    public final NState target;
    public final Symbol symbol;
    public final ActionList actions;
    public Badge badge;

    public NTransition(NState source, NState target, Symbol symbol, Badge badge) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        this.badge = badge;
        this.actions = new ActionList();
    }
}
