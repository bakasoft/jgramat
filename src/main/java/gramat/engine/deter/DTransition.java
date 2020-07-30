package gramat.engine.deter;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;
import gramat.engine.control.Check;

import java.util.Objects;

public class DTransition {

    public final DState target;
    public final Symbol symbol;
    public final Check check;
    public final ActionList actions;

    public DTransition(DState target, Symbol symbol, Check check) {
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.check = check;
        this.actions = new ActionList();
    }

}
