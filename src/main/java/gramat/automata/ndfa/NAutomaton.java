package gramat.automata.ndfa;

import gramat.output.GrammarWriter;
import gramat.output.Writable;
import gramat.util.GramatWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NAutomaton implements Writable {

    public final Language language;
    public final NState initial;
    public final Set<NState> accepted;

    NAutomaton(Language language, NState initial, Set<NState> accepted) {
        this.language = language;
        this.initial = initial;
        this.accepted = accepted;
    }

    public DState compile() {
        System.out.println("NDFA -----------");
        System.out.println(this.captureOutput());

        var dfa = DMaker.transform(this);

        System.out.println("DFA -----------");
        System.out.println(dfa.captureOutput());

        return DCompiler.transform(dfa);
    }

    @Override
    public void write(Appendable output) throws IOException {
        var states = list_states(initial);

        for (var state : states) {
            var actions = new ArrayList<>(state.actions);

            if (actions.isEmpty()) {
                actions.add(null);
            }

            for (var action : actions) {
                if (state == initial) {
                    output.append("I ");
                } else if (accepted.contains(state)) {
                    output.append("A ");
                } else {
                    output.append("S ");
                }
                output.append(String.valueOf(state.id));

                if (action != null) {
                    output.append(" ! ");
                    output.append(action.toString());
                }

                output.append("\n");
            }
        }

        for (var state : states) {
            for (var trn : state.getTransitions()) {
                var actions = new ArrayList<>(trn.actions);

                if (actions.isEmpty()) {
                    actions.add(null);
                }

                for (var action : actions) {
                    output.append("T ");
                    output.append(String.valueOf(trn.source.id));
                    output.append(" -> ");
                    output.append(String.valueOf(trn.target.id));

                    if (trn.symbol != null) {
                        output.append(" : ");
                        output.append(GramatWriter.toDelimitedString(trn.symbol.toString(), '\"'));
                    }

                    if (action != null) {
                        output.append(" ! ");
                        output.append(GramatWriter.toDelimitedString(action.toString(), '\"'));
                    }

                    output.append("\n");
                }
            }
        }
    }

    public Set<NState> getStates() {
        return list_states(initial);
    }

    private static Set<NState> list_states(NState initial) {
        var queue = new LinkedList<NState>();
        var result = new HashSet<NState>();

        queue.add(initial);

        do {
            var current = queue.remove();

            if (result.add(current)) {
                for (var trn : current.getTransitions()) {
                    queue.add(trn.target);
                }
            }
        } while(queue.size() > 0);

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

    public Set<NState> getRejected() {
        var states = new HashSet<>(getStates());

        states.removeAll(accepted);

        return states;
    }
}
