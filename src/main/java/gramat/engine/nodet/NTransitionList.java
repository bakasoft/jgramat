package gramat.engine.nodet;

import gramat.engine.symbols.Symbol;

import java.util.*;

public class NTransitionList extends ArrayList<NTransition> {

    public NTransitionList() {

    }

    public NTransitionList(Collection<NTransition> source) {
        super(source);
    }

    public NTransitionList copy() {
        return new NTransitionList(this);
    }

    public boolean hasWilds() {
        return stream().anyMatch(NTransition::isSymbolWild);
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
            result.add(transition.getSymbol());
        }
        return result;
    }

    public NTransitionList sublistBySymbol(Symbol symbol) {
        var result = new NTransitionList();
        for (var transition : this) {
            if (Objects.equals(transition.getSymbol(), symbol)) {
                result.add(transition);
            }
        }
        return result;
    }
}
