package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.util.Chain;

public class PlugTargetToSource extends Plug {

    public PlugTargetToSource(Link link) {
        super(link.symbol, link.preActions, link.postActions);
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Chain<Action> wrappingBefore, Chain<Action> wrappingAfter) {
        return graph.createLink(
                newTarget, newSource,
                wrappingBefore, wrappingAfter,  // TODO validate actions
                symbol, newBadge, BadgeMode.NONE);
    }

}
