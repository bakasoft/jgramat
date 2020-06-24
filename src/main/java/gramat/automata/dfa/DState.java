package gramat.automata.dfa;

import gramat.output.GrammarWriter;
import gramat.util.AmWriter;

import java.util.*;

public class DState {

    public boolean accepted;

    public final List<DTransition> transitions;

    public final List<DState> options;

    public final String id;

    public DState(String id) {
        this.id = id;
        transitions = new ArrayList<>();
        options = new ArrayList<>();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isFinal() {
        return transitions.isEmpty();
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

    public String getAmCode() {
        return AmWriter.getAmCode(this);
    }

}
