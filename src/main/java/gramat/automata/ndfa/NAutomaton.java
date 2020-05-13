package gramat.automata.ndfa;

import gramat.output.Writable;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class NAutomaton implements Writable {

    public final Language language;
    public final NState initial;
    public final Set<NState> accepts;

    NAutomaton(Language language, NState initial, Set<NState> accepts) {
        this.language = language;
        this.initial = initial;
        this.accepts = accepts;
    }

    public DState compile() {
        System.out.println("RAW -----------");
        System.out.println(captureOutput());

        DWildResolver.resolve(this);

        System.out.println("NO WILD -----------");
        System.out.println(captureOutput());

        var dfa = DMaker.transform(this);

        System.out.println("DFA -----------");
        System.out.println(dfa.captureOutput());

        return DCompiler.transform(dfa);
    }

    @Override
    public void write(Appendable output) throws IOException {
        output.append("{\n");
        output.append("  initial: ");
        initial.write(output);
        output.append("\n");

        output.append("  accepted:\n");
        for (var accept : accepts) {
            output.append("  - ");
            accept.write(output);
            output.append("\n");
        }

        output.append("  transitions:\n");
        for (var state : list_states(initial)) {
            for (var trn : state.getTransitions()) {
                output.append("  - ");
                trn.write(output);
                output.append("\n");
            }
        }

        output.append("}\n");
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
            if (accepts.contains(state)) {
                return true;
            }
        }

        return false;
    }
}
