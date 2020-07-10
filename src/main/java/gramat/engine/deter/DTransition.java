package gramat.engine.deter;

import gramat.engine.ActionList;
import gramat.engine.Badge;
import gramat.engine.Symbol;

import java.util.Objects;

public class DTransition {

    public final DState target;
    public final Symbol symbol;
    public final Badge badge;
    public final ActionList actions;

    public DTransition(DState target, Symbol symbol, Badge badge) {
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.badge = badge;  // this can be null
        this.actions = new ActionList();
    }

}
