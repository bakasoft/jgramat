package gramat.automata.ndfa;

import gramat.util.AmWriter;

import java.util.ArrayList;
import java.util.List;

public class NState {

    public final NLanguage language;
    public final int id;

    public final List<NAutomaton> automata;

    NState(NLanguage language, int id) {
        this.language = language;
        this.id = id;
        this.automata = new ArrayList<>();
    }

    public NStateSet getNullClosure() {
        return language.computeNullClosure(this);
    }

    public List<NTransition> getTransitions() {
        return language.findTransitionsBySource(this);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
