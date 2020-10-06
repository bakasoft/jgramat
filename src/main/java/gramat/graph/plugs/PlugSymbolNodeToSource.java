package gramat.graph.plugs;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.LinkSymbol;
import gramat.graph.Node;

public class PlugSymbolNodeToSource extends PlugSymbol {

    public final Node source;

    public PlugSymbolNodeToSource(LinkSymbol link, Node source) {
        super(link.symbol, link.beforeActions, link.afterActions);
        this.source = source;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionStore wrappingBefore, ActionStore wrappingAfter) {
        return graph.createLink(
                source, newTarget,
                beforeActions, afterActions,
                symbol, newBadge, BadgeMode.POP);
    }

}
