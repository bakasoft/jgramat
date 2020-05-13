package gramat.automata.ndfa;

import gramat.output.GrammarWriter;

public class DState {

    boolean accepted;

    DTransition[] transitions;

    DState wildState;

    public DState move(char symbol) {
        for (var trn : transitions) {
            if (trn.accepts(symbol)) {
                return trn.target;
            }
        }
        return wildState;
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
