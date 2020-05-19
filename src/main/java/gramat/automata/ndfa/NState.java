package gramat.automata.ndfa;

import java.util.List;

public class NState {

    public final NLanguage language;
    public final int id;

    NState(NLanguage language, int id) {
        this.language = language;
        this.id = id;
    }

    public List<NTransition> getTransitions() {
        return language.findTransitionsBySource(this);
    }

}
