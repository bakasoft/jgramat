package gramat.automata.dfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;
import gramat.util.AmWriter;
import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.util.*;
import java.util.function.Function;

public class DState {

    public boolean accepted;

    public final List<DTransition> transitions;

    public DState() {
        transitions = new ArrayList<>();
    }

    public DState move(int symbol, List<Action> actions) {
        DState wild = null;

        for (var trn : transitions) {
            if (trn instanceof DTransitionWild) {
                wild = trn.target;
            }
            else if (trn.accepts(symbol)) {
                actions.addAll(trn.actions);
                return trn.target;
            }
        }

        return wild;
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
