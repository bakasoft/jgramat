package gramat.automata.ndfa;

import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;
import gramat.util.GramatWriter;

import java.io.IOException;
import java.util.*;

public class NMachine {

    public final NLanguage language;
    public final NState[] states;
    public final NTransition[] transitions;
    public final NState[] initial;
    public final NState[] accepted;
    public final NState[] rejected;

    public NMachine(
            NLanguage language,
            NState[] states,
            NTransition[] transitions,
            NState[] initial,
            NState[] accepted,
            NState[] rejected) {
        this.language = language;
        this.states = states;
        this.transitions = transitions;
        this.initial = initial;
        this.accepted = accepted;
        this.rejected = rejected;
    }

    public void write(Appendable output) throws IOException {
        var states = list_states(initial);

        for (var state : initial) {
            output.append("I ");
            output.append(String.valueOf(state.id));
            output.append("\n");
        }

        for (var state : states) {
            if (Set.of(accepted).contains(state)) {
                output.append("A ");
            } else {
                output.append("S ");
            }
            output.append(String.valueOf(state.id));
            output.append("\n");
        }

        for (var state : states) {
            for (var trn : state.getTransitions()) {
                output.append("T ");
                output.append(String.valueOf(trn.source.id));
                output.append(" -> ");
                output.append(String.valueOf(trn.target.id));
                output.append(" : ");
                output.append(GramatWriter.toDelimitedString(trn.symbol.toString(), '\"'));
                output.append("\n");
            }
        }
    }

    private static Set<NState> list_states(NState[] initial) {
        var queue = new LinkedList<>(List.of(initial));
        var result = new HashSet<NState>();

        while(queue.size() > 0) {
            var current = queue.remove();

            if (result.add(current)) {
                for (var trn : current.getTransitions()) {
                    queue.add(trn.target);
                }
            }
        }

        return result;
    }
}
