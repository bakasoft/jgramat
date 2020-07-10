package gramat.engine.nodet;

import gramat.engine.Badge;

import java.util.ArrayList;
import java.util.List;

public class NState {

    public final NRoot root;
    public final String id;

    public final List<NMark> marks;

    public NState(NRoot root, String id) {
        this.root = root;
        this.id = id;
        this.marks = new ArrayList<>();
    }

    public NTransitionList getTransitions() {
        return root.findTransitionsBySource(this);
    }

    public NStateList getEmptyClosure(Badge badge) {
        return root.computeEmptyClosure(this, badge);
    }

    public NStateList getInverseEmptyClosure() {
        return root.computeInverseEmptyClosure(this);
    }
}
