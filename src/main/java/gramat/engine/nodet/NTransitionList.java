package gramat.engine.nodet;

import gramat.engine.Badge;
import gramat.engine.SymbolWild;

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

    public NStateList collectTargets(Badge badge) {
        var result = new NStateList();
        for (var transition : this) {
            if (transition.badge == null || transition.badge == badge) {
                result.add(transition.target);
            }
        }
        return result;
    }

    public List<Badge> collectBadges() {
        var badges = new ArrayList<Badge>();

        for (var transition : this) {
            if (transition.badge != null && !badges.contains(transition.badge)) {
                badges.add(transition.badge);
            }
        }

        return badges;
    }
}
