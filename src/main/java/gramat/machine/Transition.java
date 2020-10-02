package gramat.machine;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.symbols.Symbol;

public class Transition {

    public final State target;
    public final Symbol symbol;

    public ActionStore before;  // TODO make immutable
    public ActionStore after;  // TODO make immutable

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
