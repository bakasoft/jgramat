package gramat.automata.ndfa;

import gramat.automata.actions.Action;
import gramat.output.GrammarWriter;

import java.util.Collections;
import java.util.List;

public class DState {

    boolean accepted;

    DTransition[] transitions;

    public Action[] actions; // TODO remove public

    DTransition wildTransition;

    public DState move(char symbol, List<Action> actions) {
        for (var trn : transitions) {
            if (trn.accepts(symbol)) {
                Collections.addAll(actions, trn.actions);
                return trn.target;
            }
        }

        if (wildTransition != null) {
            Collections.addAll(actions, wildTransition.actions);
            return wildTransition.target;
        }

        return null;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isFinal() {
        return transitions.length == 0;
    }

    public void write(GrammarWriter writer) {
        if (writer.open(this, "state")) {
            writer.attribute("accepted", accepted);
            for (var t : transitions) {
                t.write(writer);
            }
            writer.close();
        }
    }
}
