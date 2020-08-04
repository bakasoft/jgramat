package gramat.engine.indet;

import gramat.engine.actions.ActionList;
import gramat.engine.checks.Check;
import gramat.engine.symbols.Symbol;

import java.util.Objects;

public class ITransition {

    public final IState source;
    public final IState target;
    public final Symbol symbol;
    public final Check check;
    public final ActionList actions;

    public ITransition(IState source, IState target, Symbol symbol, Check check) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.symbol = Objects.requireNonNull(symbol);
        this.check = Objects.requireNonNull(check);
        this.actions = new ActionList();
    }
}
