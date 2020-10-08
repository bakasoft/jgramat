package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;

public class PlugSymbolNodeToTarget extends PlugSymbol {

    public final Node source;

    public PlugSymbolNodeToTarget(Link link, Node source) {
        super(link.symbol, link.beforeActions, link.afterActions);
        this.source = source;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionStore wrappingBefore, ActionStore wrappingAfter) {
        return graph.createLink(
                source, newTarget,
                beforeActions, ActionStore.join(afterActions, wrappingAfter),
                symbol, newBadge, BadgeMode.POP);
    }
}
