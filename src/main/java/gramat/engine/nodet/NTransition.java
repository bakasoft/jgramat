package gramat.engine.nodet;

import gramat.engine.actions.Action;
import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;
import gramat.engine.control.Check;
import gramat.engine.symbols.SymbolNull;
import gramat.engine.symbols.SymbolWild;

import java.util.Objects;

public class NTransition {

    public final NState source;
    public final NState target;

    private Symbol symbol;
    private Check check;
    public final ActionList actions;

    public NTransition(NState source, NState target, Symbol symbol, Check check) {
        this.source = source;
        this.target = target;
        this.symbol = Objects.requireNonNull(symbol);
        this.check = Objects.requireNonNull(check);
        this.actions = new ActionList();
    }

    public boolean isSymbolNull() {
        return symbol.isNull();
    }

    public boolean isSymbolWild() {
        return symbol.isWild();
    }

    public boolean isSymbol(Symbol symbol) {
        return Objects.equals(this.symbol, symbol);
    }

    public boolean isCheck(Check check) {
        return Objects.equals(this.check, check);
    }

    public boolean isCheckNull() {
        return check.isNull();
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }
}
