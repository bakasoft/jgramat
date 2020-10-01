package gramat.proto;

import gramat.actions.ActionStore;

import java.util.LinkedHashSet;
import java.util.Objects;

abstract public class Link {

    public Node source;
    public Node target;

    public final ActionStore beforeActions;
    public final ActionStore afterActions;

    public Link(Node source, Node target) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.beforeActions = new ActionStore();
        this.afterActions = new ActionStore();
    }

    public static NodeSet collectTargets(Iterable<? extends Link> links) {
        var targets = new LinkedHashSet<Node>();

        for (var link : links) {
            targets.add(link.target);
        }

        return new NodeSet(targets);
    }

}
