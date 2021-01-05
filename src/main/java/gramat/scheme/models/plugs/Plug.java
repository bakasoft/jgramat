package gramat.scheme.models.plugs;

import gramat.scheme.common.actions.Event;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.models.Link;
import gramat.scheme.models.Node;
import gramat.scheme.common.symbols.Symbol;

import java.util.Objects;

public class Plug {

    public static Plug make(Link link, PlugType dir) {
        switch(dir) {
            case SOURCE_TO_TARGET:
            case TARGET_TO_SOURCE:
            case SOURCE_TO_SOURCE:
            case TARGET_TO_TARGET:
                return new Plug(dir, link.symbol, link.event, null, null);
            case SOURCE_TO_NODE:
            case TARGET_TO_NODE:
                return new Plug(dir, link.symbol, link.event, null, link.target);
            case NODE_TO_SOURCE:
            case NODE_TO_TARGET:
                return new Plug(dir, link.symbol, link.event, link.source, null);
            default:
                throw new UnsupportedValueException(dir);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plug plug = (Plug) o;
        return type == plug.type &&
                Objects.equals(symbol, plug.symbol) &&
                Objects.equals(source, plug.source) &&
                Objects.equals(target, plug.target) &&
                Objects.equals(event, plug.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, symbol, source, target, event);
    }
}
