package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.LinkSymbol;
import gramat.graph.Node;

public class PlugSymbolTargetToNode extends PlugSymbol {

    public final Node target;

    public PlugSymbolTargetToNode(LinkSymbol link, Node target) {
        super(link.symbol, link.beforeActions, link.afterActions);
        this.target = target;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionStore wrappingBefore, ActionStore wrappingAfter) {
        return graph.createLink(
                newTarget, target,
                beforeActions, afterActions,
                symbol, newBadge, BadgeMode.PUSH);
    }
}
