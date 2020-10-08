package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;

public class PlugSymbolSourceToNode extends PlugSymbol {

    public final Node target;

    public PlugSymbolSourceToNode(Link link, Node target) {
        super(link.symbol, link.beforeActions, link.afterActions);
        this.target = target;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionStore wrappingBefore, ActionStore wrappingAfter) {
        return graph.createLink(
                newSource, target,
                ActionStore.join(wrappingBefore, beforeActions), afterActions,
                symbol, newBadge, BadgeMode.PUSH);
    }
}
