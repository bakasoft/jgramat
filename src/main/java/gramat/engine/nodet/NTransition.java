package gramat.engine.nodet;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;
import gramat.engine.checks.Check;

import java.util.Objects;

public class NTransition {

    public final NState source;
    public final NState target;

    public final Symbol symbol;
    public final Check check;

    public final ActionList actions;

    public NTransition(NState source, NState target, Symbol symbol, Check check) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        this.check = check;
        this.actions = new ActionList();
    }

}
