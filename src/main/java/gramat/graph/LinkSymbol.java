package gramat.graph;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;

public class LinkSymbol extends Link {
    public final Symbol symbol;
    public final Badge badge;
    public final BadgeMode mode;
    public LinkSymbol(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, Symbol symbol, Badge badge, BadgeMode mode) {
        super(source, target, beforeActions, afterActions);
        this.symbol = symbol;
        this.badge = badge;
        this.mode = mode;
    }
}
