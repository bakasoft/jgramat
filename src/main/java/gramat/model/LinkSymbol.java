package gramat.model;

import gramat.actions.Action;
import gramat.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinkSymbol extends Link {

    public final Symbol symbol;

    public final List<Action> beforeActions;
    public final List<Action> afterActions;

    public LinkSymbol(Node source, Node target, Symbol symbol) {
        super(source, target);
        this.symbol = Objects.requireNonNull(symbol);
        this.beforeActions = new ArrayList<>();
        this.afterActions = new ArrayList<>();
    }

}
