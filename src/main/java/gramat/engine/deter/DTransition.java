package gramat.engine.deter;

import gramat.engine.ActionList;
import gramat.engine.Symbol;
import gramat.engine.stack.ControlCheck;

import java.util.Objects;

public class DTransition {

    public final DState target;
    public final Symbol symbol;
    public final ControlCheck check;
    public final ActionList actions;

    public DTransition(DState target, Symbol symbol, ControlCheck check) {
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.check = check;
        this.actions = new ActionList();
    }

}
