package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.actions.Event;
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
    private final Event event;
    private final Node source;
    private final Node target;

    private Plug(PlugType type, Symbol symbol, Event event, Node source, Node target) {
        this.type = Objects.requireNonNull(type);
        this.symbol = Objects.requireNonNull(symbol);
        this.event = Objects.requireNonNull(event);
        this.source = source;
        this.target = target;
    }

    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Event wrapper) {
        switch(type) {
            case S2T:
                return graph.createLink(
                        newSource, newTarget,
                        Event.of(wrapper, event),
                        symbol, newBadge, BadgeMode.NONE);
            case S2N:
                return graph.createLink(
                        newSource, target,
                        Event.of(wrapper.before, event, null),
                        symbol, newBadge, BadgeMode.PUSH);
            case T2S:
                return graph.createLink(
                        newTarget, newSource,
                        event,
                        symbol, newBadge, BadgeMode.NONE);
            case T2N:
                return graph.createLink(
                        newTarget, target,
                        event,
                        symbol, newBadge, BadgeMode.PUSH);
            case N2S:
                return graph.createLink(
                        source, newTarget,
                        event,
                        symbol, newBadge, BadgeMode.POP);
            case N2T:
                return graph.createLink(
                        source, newTarget,
                        Event.of(null, event, wrapper.after),
                        symbol, newBadge, BadgeMode.POP);
            default:
                throw new UnsupportedValueException(type, "link direction");
        }
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

    public Event getEvent() {
        return event;
    }
}
