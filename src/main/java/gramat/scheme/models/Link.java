package gramat.scheme.models;

import gramat.scheme.common.actions.Event;
import gramat.scheme.common.badges.Badge;
import gramat.scheme.models.sets.NodeSet;
import gramat.scheme.common.symbols.Symbol;
import gramat.util.DataUtils;

import java.util.List;
import java.util.Objects;

public class Link {

    public Node source;
    public Node target;

    public final Symbol symbol;
    public final Badge badge;

    public final Event event;

    public Link(Node source, Node target, Event event, Symbol symbol, Badge badge) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.badge = Objects.requireNonNull(badge);
        this.event = Objects.requireNonNull(event);
    }

    public static NodeSet collectTargets(List<? extends Link> links) {
        return NodeSet.of(DataUtils.map(links, link -> link.target));
    }

    @Override
    public String toString() {
        return source.id + "->" + target.id + " : " + symbol;
    }

}
