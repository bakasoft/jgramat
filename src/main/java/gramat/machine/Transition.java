package gramat.machine;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;

import java.util.Objects;

public class Transition {

    public final State target;
    public final Symbol symbol;
    public final Badge badge;
    public final BadgeMode mode;
    public final Action[] before;
    public final Action[] after;

    public Transition(Symbol symbol, Badge badge, BadgeMode mode, State target, Action[] before, Action[] after) {
        this.symbol = Objects.requireNonNull(symbol);
        this.badge = Objects.requireNonNull(badge);
        this.mode = Objects.requireNonNull(mode);
        this.target = Objects.requireNonNull(target);
        this.before = before;
        this.after = after;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public State getTarget() {
        return target;
    }
}
