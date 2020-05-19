package gramat.automata.ndfa;

import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;
import gramat.util.GramatWriter;

import java.io.IOException;
import java.util.*;

public class NMachine {

    public final NLanguage language;
    public final List<NState> states;
    public final List<NTransition> transitions;
    public final List<NState> initial;
    public final List<NState> accepted;

    public NMachine(
            NLanguage language,
            List<NState> states,
            List<NTransition> transitions,
            List<NState> initial,
            List<NState> accepted) {
        this.language = language;
        this.states = Collections.unmodifiableList(states);
        this.transitions = Collections.unmodifiableList(transitions);
        this.initial = Collections.unmodifiableList(initial);
        this.accepted = Collections.unmodifiableList(accepted);
    }


    public DState compile() {
//        System.out.println("NDFA -----------");
//        System.out.println(this.captureOutput());
        return DMaker.transform(this);
    }

    public void write(Appendable output) throws IOException {
        var states = list_states(initial);

        for (var state : initial) {
            output.append("I ");
            output.append(String.valueOf(state.id));
            output.append("\n");
        }

        for (var state : states) {
            if (accepted.contains(state)) {
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
                if (trn.symbol != null) {
                    output.append(" : ");
                    output.append(GramatWriter.toDelimitedString(trn.symbol.toString(), '\"'));
                }
                output.append("\n");
            }
        }
    }

    private static Set<NState> list_states(Collection<NState> initial) {
        var queue = new LinkedList<>(initial);
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
