package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.util.Chain;

public class PlugSourceToTarget extends Plug {

    public PlugSourceToTarget(Link link) {
        super(link.symbol, link.preActions, link.postActions);
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Chain<Action> wrappingBefore, Chain<Action> wrappingAfter) {
        return graph.createLink(
                newSource, newTarget,
                Chain.merge(wrappingBefore, beforeActions),
                Chain.merge(afterActions, wrappingAfter),
                symbol, newBadge, BadgeMode.NONE);
    }

}
