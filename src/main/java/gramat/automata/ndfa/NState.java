package gramat.automata.ndfa;

import gramat.eval.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NState {

    public final Language language;
    public final int id;

    NState(Language language, int id) {
        this.language = language;
        this.id = id;
    }

    public List<NTransition> getTransitions() {
        return language.transitions.stream()
                .filter(t -> t.source == this)
                .collect(Collectors.toUnmodifiableList());
    }

    public void write(Appendable output) throws IOException {
        output.append("S");
        output.append(String.valueOf(id));
    }
}
