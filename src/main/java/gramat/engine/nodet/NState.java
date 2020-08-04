package gramat.engine.nodet;

public class NState {

    public final NRoot root;
    public final String id;

    public NState(NRoot root, String id) {
        this.root = root;
        this.id = id;
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
