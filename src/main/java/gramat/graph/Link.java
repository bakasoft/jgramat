package gramat.graph;

import gramat.actions.ActionStore;

import java.util.LinkedHashSet;
import java.util.Objects;

abstract public class Link {

    public final Node source;
    public final Node target;

    public final ActionStore beforeActions;
    public final ActionStore afterActions;

    public Link(Node source, Node target, ActionStore beforeActions, ActionStore afterActions) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.beforeActions = Objects.requireNonNull(beforeActions);
        this.afterActions = Objects.requireNonNull(afterActions);
    }

    public static NodeSet collectTargets(Iterable<? extends Link> links) {
        var targets = new LinkedHashSet<Node>();

        for (var link : links) {
            targets.add(link.target);
        }

        return new NodeSet(targets);
    }

}
