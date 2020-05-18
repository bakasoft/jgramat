package gramat.automata.ndfa;

import gramat.output.Writable;
import gramat.util.GramatWriter;

import java.io.IOException;
import java.util.*;

public class NAutomaton implements Writable {

    public final Language language;
    public final Set<NState> initial;
    public final Set<NState> accepted;
    public final Set<NState> states;

    NAutomaton(Language language, Set<NState> initial, Set<NState> accepted, Set<NState> states) {
        this.language = language;
        this.initial = initial;
        this.accepted = accepted;
        this.states = states;
    }

    public DState compile() {
//        System.out.println("NDFA -----------");
//        System.out.println(this.captureOutput());
        return DMaker.transform(this);
    }

    @Override
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

    private static Set<NState> list_states(Set<NState> initial) {
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

    public boolean isAccepted(Set<NState> states) {
        for (var state : states) {
            if (accepted.contains(state)) {
                return true;
            }
        }

        return false;
    }
}
