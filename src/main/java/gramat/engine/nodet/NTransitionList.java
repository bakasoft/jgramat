package gramat.engine.nodet;

import gramat.engine.SymbolWild;
import gramat.engine.stack.ControlCheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public NStateList collectTargets() {
        var result = new NStateList();
        for (var transition : this) {
            result.add(transition.target);
        }
        return result;
    }

    public List<ControlCheck> collectChecks() {
        var checks = new ArrayList<ControlCheck>();

        for (var transition : this) {
            if (transition.check != null && !checks.contains(transition.check)) {
                checks.add(transition.check);
            }
        }

        return checks;
    }
}
