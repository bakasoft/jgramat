package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.util.Chain;

public class PlugNodeToSource extends Plug {

    public final Node source;

    public PlugNodeToSource(Link link, Node source) {
        super(link.symbol, link.preActions, link.postActions);
        this.source = source;
    }

    @Override
    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Chain<Action> wrappingBefore, Chain<Action> wrappingAfter) {
        return graph.createLink(
                source, newTarget,
                beforeActions, afterActions,
                symbol, newBadge, BadgeMode.POP);
    }

}
