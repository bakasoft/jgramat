package gramat.eval;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.symbols.Symbol;

import java.io.PrintStream;

public class Transition {

    protected final State target;
    protected final Symbol symbol;

    protected ActionStore before;
    protected ActionStore after;

    public Transition(Symbol symbol, State target) {
        this.symbol = symbol;
        this.target = target;
    }

    public boolean addBefore(Action action) {
        if (before == null) {
            before = new ActionStore();
        }

        return before.add(action);
    }

    public boolean addAfter(Action action) {
        if (after == null) {
            after = new ActionStore();
        }

        return after.add(action);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public State getTarget() {
        return target;
    }
}