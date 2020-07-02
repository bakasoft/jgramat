package gramat.engine.nodet;

import gramat.engine.SymbolWild;

import java.util.ArrayList;
import java.util.Collection;

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
        return stream().anyMatch(s -> s.symbol instanceof SymbolWild);
    }
}
