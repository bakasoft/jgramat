package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.util.Chain;

public class PlugSourceToNode extends Plug {

    public final Node target;

    public PlugSourceToNode(Link link, Node target) {
        super(link.symbol, link.preActions, link.postActions);
        this.target = target;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Chain<Action> wrappingBefore, Chain<Action> wrappingAfter) {
        return graph.createLink(
                newSource, target,
                Chain.merge(wrappingBefore, beforeActions), afterActions,
                symbol, newBadge, BadgeMode.PUSH);
    }
}
