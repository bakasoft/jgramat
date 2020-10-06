package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.symbols.Symbol;

public abstract class PlugSymbol extends Plug {

    public final Symbol symbol;

    protected PlugSymbol(Symbol symbol, ActionStore beforeActions, ActionStore afterActions) {
        super(beforeActions, afterActions);
        this.symbol = symbol;
    }
}
