package gramat.automata.ndfa;

import gramat.automata.actions.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NState {

    public final Language language;
    public final int id;
    public final List<Action> actions;

    NState(Language language, int id) {
        this.language = language;
        this.id = id;
        this.actions = new ArrayList<>();
    }

    public List<NTransition> getTransitions() {
        return language.transitions.stream()
                .filter(t -> t.source == this)
                .collect(Collectors.toUnmodifiableList());
    }

    public void write(Appendable output) throws IOException {
        output.append("S");
        output.append(String.valueOf(id));
        for (var action : actions) {
            output.append(" ");
            output.append(action.toString());
        }
    }
}
