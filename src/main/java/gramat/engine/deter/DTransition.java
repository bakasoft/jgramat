package gramat.engine.deter;

import gramat.engine.actions.Action;
import gramat.engine.symbols.Symbol;

import java.util.Objects;

public class DTransition {

    public final Symbol symbol;
    public final DState target;
    public final Action[] actions;

    public DTransition(Symbol symbol, DState target, Action[] actions) {
        this.symbol = Objects.requireNonNull(symbol);
        this.target = Objects.requireNonNull(target);
        this.actions = Objects.requireNonNull(actions);
    }

}
