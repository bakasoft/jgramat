package gramat.graph;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;

import java.util.Objects;

public class LinkSymbol extends Link {
    public final Symbol symbol;
    public final Badge badge;
    public final BadgeMode mode;
    public LinkSymbol(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, Symbol symbol, Badge badge, BadgeMode mode) {
        super(source, target, beforeActions, afterActions);
        this.symbol = Objects.requireNonNull(symbol);
        this.badge = Objects.requireNonNull(badge);
        this.mode = Objects.requireNonNull(mode);
    }
}
