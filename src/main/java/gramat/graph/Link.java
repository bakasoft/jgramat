package gramat.graph;

import gramat.actions.ActionStore;
import gramat.util.Chain;
import gramat.util.DataUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public abstract class Link {

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

    public static Chain<Node> collectTargets(List<? extends Link> links) {
        return Chain.of(DataUtils.map(links, link -> link.target));
    }

    @Override
    public abstract String toString();
}
