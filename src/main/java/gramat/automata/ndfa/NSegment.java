package gramat.automata.ndfa;

import gramat.util.AmWriter;

public class NSegment {

    public final NLanguage language;
    public final NState initial;
    public final NState accepted;

    public NSegment(NLanguage language, NState initial, NState accepted) {
        this.language = language;
        this.initial = initial;
        this.accepted = accepted;
    }

    public void wrap(NState beforeInitial, NState afterAccepted) {
        language.transition(beforeInitial, initial, null);
        language.transition(accepted, afterAccepted, null);
    }

    public NMachine toMachine(NGroup group) {
        return new NMachine(this, group);
    }

    public String getAmCode() {
        return AmWriter.getAmCode(this);
    }
}
