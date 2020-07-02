package gramat.engine.nodet;

import java.util.ArrayList;
import java.util.List;

public class NState {

    public final NRoot root;
    public final int id;

    public final List<NMark> marks;

    public NState(NRoot root, int id) {
        this.root = root;
        this.id = id;
        this.marks = new ArrayList<>();
    }

    public NTransitionList getTransitions() {
        return root.findTransitionsBySource(this);
    }

    public NStateList getEmptyClosure() {
        return root.computeEmptyClosure(this);
    }

    public NStateList getInverseEmptyClosure() {
        return root.computeInverseEmptyClosure(this);
    }
}
