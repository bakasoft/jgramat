package gramat.engine.deter;

import gramat.engine.ActionList;
import gramat.engine.Badge;
import gramat.engine.Symbol;

public class DTransition {

    public final DState target;
    public final Symbol symbol;
    public final Badge badge;
    public final ActionList actions;

    public DTransition(DState target, Symbol symbol, Badge badge) {
        this.target = target;
        this.symbol = symbol;
        this.badge = badge;
        this.actions = new ActionList();
    }

}
