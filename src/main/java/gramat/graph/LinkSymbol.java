package gramat.graph;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.symbols.Symbol;

public class LinkSymbol extends Link {
    public final Symbol symbol;
    public final Badge badge;
    public LinkSymbol(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, Symbol symbol, Badge badge) {
        super(source, target, beforeActions, afterActions);
        this.symbol = symbol;
        this.badge = badge;
    }
}
