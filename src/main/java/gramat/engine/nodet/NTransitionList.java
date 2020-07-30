package gramat.engine.nodet;

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
        return stream().anyMatch(NTransition::isSymbolWild);
    }

    public NStateList collectTargets() {
        var result = new NStateList();
        for (var transition : this) {
            result.add(transition.target);
        }
        return result;
    }
}
