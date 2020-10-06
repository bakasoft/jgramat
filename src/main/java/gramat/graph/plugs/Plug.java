package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;

public abstract class Plug {

    public final ActionStore beforeActions;
    public final ActionStore afterActions;

    protected Plug(ActionStore beforeActions, ActionStore afterActions) {
        this.beforeActions = beforeActions;
        this.afterActions = afterActions;
    }

    public abstract Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionStore wrappingBefore, ActionStore wrappingAfter);

}
