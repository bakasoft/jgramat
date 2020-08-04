package gramat.engine.nodet;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;

public class NTransition {

    public final NState source;
    public final NState target;

    public final Symbol symbol;

    public final ActionList actions;

    public NTransition(NState source, NState target, Symbol symbol) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        this.actions = new ActionList();
    }

}
