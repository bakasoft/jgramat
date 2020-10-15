package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.actions.Event;
import gramat.actions.RecursionEnter;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.symbols.Symbol;
import gramat.util.Chain;

import java.util.Objects;

public class Plug {

    public static Plug make(Link link, PlugType dir) {
        switch(dir) {
            case S2T:
            case T2S:
                return new Plug(dir, link.symbol, link.event, null, null);
            case S2N:
            case T2N:
                return new Plug(dir, link.symbol, link.event, null, link.target);
            case N2S:
            case N2T:
                return new Plug(dir, link.symbol, link.event, link.source, null);
            default:
                throw new UnsupportedValueException(dir, "link direction");
        }
    }

    private final PlugType type;
    private final Symbol symbol;
    private final Node source;
    private final Node target;

    public final Event event;

    private Plug(PlugType type, Symbol symbol, Event event, Node source, Node target) {
        this.type = Objects.requireNonNull(type);
        this.symbol = Objects.requireNonNull(symbol);
        this.event = Objects.requireNonNull(event);
        this.source = source;
        this.target = target;
    }

    public PlugType getType() {
        return type;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Node getSource() {
        return Objects.requireNonNull(source);
    }

    public Node getTarget() {
        return Objects.requireNonNull(target);
    }

}
