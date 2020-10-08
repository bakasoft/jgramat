package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.symbols.Symbol;
import gramat.util.Chain;

public abstract class Plug {

    public final Symbol symbol;
    public final Chain<Action> beforeActions;
    public final Chain<Action> afterActions;

    protected Plug(Symbol symbol, Chain<Action> beforeActions, Chain<Action> afterActions) {
        this.symbol = symbol;
        this.beforeActions = beforeActions;
        this.afterActions = afterActions;
    }

    public abstract Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, Chain<Action> wrappingBefore, Chain<Action> wrappingAfter);

}
