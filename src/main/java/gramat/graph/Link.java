package gramat.graph;

import gramat.actions.Action;
import gramat.actions.Event;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;
import gramat.util.Chain;
import gramat.util.DataUtils;

import java.util.List;
import java.util.Objects;

public class Link {

    public final Node source;
    public final Node target;

    public final Symbol symbol;
    public final Badge badge;
    public final BadgeMode mode;

    public Event event;

    public Link(Node source, Node target, Event event, Symbol symbol, Badge badge, BadgeMode mode) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.badge = Objects.requireNonNull(badge);
        this.mode = Objects.requireNonNull(mode);
        this.event = Objects.requireNonNull(event);
    }

    public static Chain<Node> collectTargets(List<? extends Link> links) {
        return Chain.of(DataUtils.map(links, link -> link.target));
    }

    @Override
    public String toString() {
        return source.id + "->" + target.id + " : " + symbol;
    }

}
