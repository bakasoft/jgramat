package gramat.graph.plugs;

import gramat.actions.Action;
import gramat.actions.ActionWrapper;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Graph;
import gramat.graph.Link;
import gramat.graph.Node;
import gramat.symbols.Symbol;
import gramat.util.Chain;

import java.util.Objects;

public class Plug implements ActionWrapper {

    public static Plug make(Link link, PlugType dir) {
        switch(dir) {
            case S2T:
            case T2S:
                return new Plug(dir, link.symbol, link.preActions, link.postActions, null, null);
            case S2N:
            case T2N:
                return new Plug(dir, link.symbol, link.preActions, link.postActions, null, link.target);
            case N2S:
            case N2T:
                return new Plug(dir, link.symbol, link.preActions, link.postActions, link.source, null);
            default:
                throw new UnsupportedValueException(dir, "link direction");
        }
    }

    private final PlugType type;
    private final Symbol symbol;
    private final Chain<Action> beginActions;
    private final Chain<Action> endActions;
    private final Node source;
    private final Node target;

    private Plug(PlugType type, Symbol symbol, Chain<Action> beginActions, Chain<Action> endActions, Node source, Node target) {
        this.type = Objects.requireNonNull(type);
        this.symbol = Objects.requireNonNull(symbol);
        this.beginActions = beginActions;
        this.endActions = endActions;
        this.source = source;
        this.target = target;
    }

    public Link connectTo(Graph graph, Node newSource, Node newTarget, Badge newBadge, ActionWrapper wrapper) {
        switch(type) {
            case S2T:
                return graph.createLink(
                        newSource, newTarget,
                        Chain.merge(wrapper.getBegin(), beginActions),
                        Chain.merge(endActions, wrapper.getEnd()),
                        symbol, newBadge, BadgeMode.NONE);
            case S2N:
                return graph.createLink(
                        newSource, target,
                        Chain.merge(wrapper.getBegin(), beginActions),
                        endActions,
                        symbol, newBadge, BadgeMode.PUSH);
            case T2S:
                return graph.createLink(
                        newTarget, newSource,
                        wrapper.getBegin(),
                        wrapper.getEnd(),  // TODO validate actions
                        symbol, newBadge, BadgeMode.NONE);
            case T2N:
                return graph.createLink(
                        newTarget, target,
                        beginActions,
                        endActions,
                        symbol, newBadge, BadgeMode.PUSH);
            case N2S:
                return graph.createLink(
                        source, newTarget,
                        beginActions,
                        endActions,
                        symbol, newBadge, BadgeMode.POP);
            case N2T:
                return graph.createLink(
                        source, newTarget,
                        beginActions,
                        Chain.merge(endActions, wrapper.getEnd()),
                        symbol, newBadge, BadgeMode.POP);
            default:
                throw new UnsupportedValueException(type, "link direction");
        }
    }

    public PlugType getType() {
        return type;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Node getSource() {
        return Objects.requireNonNull(source);
    }

    public Node getTarget() {
        return Objects.requireNonNull(target);
    }

    @Override
    public Chain<Action> getBegin() {
        return beginActions;
    }

    @Override
    public Chain<Action> getEnd() {
        return endActions;
    }
}
