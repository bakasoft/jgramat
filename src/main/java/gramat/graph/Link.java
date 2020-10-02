package gramat.graph;

import gramat.actions.ActionStore;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Link extends Join {

    public final Node source;
    public final Node target;

    public Link(Node source, Node target, Token token, ActionStore beforeActions, ActionStore afterActions) {
        super(token, beforeActions, afterActions);
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
    }

    public static NodeSet collectTargets(Iterable<? extends Link> links) {
        var targets = new LinkedHashSet<Node>();

        for (var link : links) {
            targets.add(link.target);
        }

        return new NodeSet(targets);
    }

}
