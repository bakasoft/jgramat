package gramat.engine.nodet;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;
import gramat.engine.symbols.SymbolCheck;
import gramat.engine.symbols.SymbolWild;
import gramat.tools.Store;

import java.util.*;

public class NTransitionList extends Store<NTransition> {

    public NTransitionList() {

    }

    public NTransitionList(NTransition trn) {
        add(trn);
    }

    @Override
    public boolean canAdd(NTransition item) {
        if (contains(item)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canRemove(NTransition item) {
        return true;
    }

    public boolean hasWilds() {
        return stream().anyMatch(trn -> hasWilds(trn.symbol));
    }

    private static boolean hasWilds(Symbol symbol) {
        if (symbol instanceof SymbolWild) {
            return true;
        }
        else if (symbol instanceof SymbolCheck) {
            return hasWilds(((SymbolCheck) symbol).symbol);
        }
        return false;
    }

    public NStateList collectSources() {
        var result = new NStateList();
        for (var transition : this) {
            result.add(transition.source);
        }
        return result;
    }

    public NStateList collectTargets() {
        var result = new NStateList();
        for (var transition : this) {
            result.add(transition.target);
        }
        return result;
    }

    public Set<Symbol> collectSymbols() {
        var result = new HashSet<Symbol>();
        for (var transition : this) {
            result.add(transition.symbol);
        }
        return result;
    }

    public NTransitionList sublistBySymbol(Symbol symbol) {
        var result = new NTransitionList();
        for (var transition : this) {
            if (Objects.equals(transition.symbol, symbol)) {
                result.add(transition);
            }
        }
        return result;
    }

    public ActionList collectActions() {
        var result = new ActionList();

        for (var item : this) {
            result.addAll(item.actions);
        }

        return result;
    }
}
