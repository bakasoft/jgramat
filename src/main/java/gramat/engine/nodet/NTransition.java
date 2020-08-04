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
        this.symbol = Objects.requireNonNull(symbol);
        this.check = Objects.requireNonNull(check);
        this.actions = new ActionList();
    }

    public boolean isSymbol(Symbol symbol) {
        return Objects.equals(this.symbol, symbol);
    }

    public boolean isCheck(Check check) {
        return Objects.equals(this.check, check);
    }

}
