package gramat.engine.nodet;

public class NState {

    public final NLanguage lang;
    public final String id;

    public NState(NLanguage lang, String id) {
        this.lang = lang;
        this.id = id;
    }

    public NTransitionList getTransitions() {
        return lang.findTransitionsBySource(this);
    }

    public NStateList getEmptyClosure() {
        return lang.computeEmptyClosure(this);
    }

    public NStateList getInverseEmptyClosure() {
        return lang.computeInverseEmptyClosure(this);
    }
}
