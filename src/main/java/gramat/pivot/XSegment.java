package gramat.pivot;

import gramat.eval.State;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class XSegment {

    public final XLanguage lang;

    public XState initial;
    public XState accepted;

    public XSegment(XLanguage lang, XState initial, XState accepted) {
        this.lang = lang;
        this.initial = initial;
        this.accepted = accepted;
    }

    public XSegment deepCopy(List<XActionBlock> blocks) {
        var oldNews = new HashMap<XState, XState>();

        var allTransitions = new ArrayList<XTransition>();

        // First, copy all states and map them for future reference
        initial.walk((state, transitions) -> {
            var newState = state.shallowCopy();

            oldNews.put(state, newState);

            allTransitions.addAll(transitions);
        });

        // Then, copy all transitions using the mapped states
        for (var transition : allTransitions) {
            var newSource = oldNews.get(transition.source);
            var newTarget = oldNews.get(transition.target);

            if (newSource == null || newTarget == null) {
                throw new RuntimeException("Deep copy failed.");
            }

            transition.shallowCopy(newSource, newTarget);
        }

        // Finally, find corresponding states for the segment
        var newInitial = oldNews.get(initial);
        var newAccepted = oldNews.get(accepted);

        if (newInitial == null || newAccepted == null) {
            throw new RuntimeException("Deep copy failed.");
        }

        // Additionally, clone the affected blocks too
        for (var block : new ArrayList<>(blocks)) {
            var newBlockInitial = oldNews.get(block.initial);
            var newBlockAccepted = oldNews.get(block.accepted);

            if (newBlockInitial != null && newBlockAccepted != null) {
                blocks.add(new XActionBlock(newBlockInitial, block.before, newBlockAccepted, block.after));
            }
            else if (newBlockInitial != null || newBlockAccepted != null) {
                throw new RuntimeException("corrupted action block?");
            }
        }

        return new XSegment(lang, newInitial, newAccepted);
    }

    public State compile(List<XActionBlock> blocks) {
        var compiler = new XSegmentCompiler(lang);

        return compiler.compile(initial, accepted, blocks);
    }

    public void printAmCode(PrintStream out) {
        out.print("-> ");
        out.println(initial.id);

        initial.walk((state, transitions) -> {
            for (var transition : transitions) {
                transition.printAmCode(out);
            }
        });

        out.print(accepted.id);
        out.println(" <=");
    }

    public List<XTransition> listTransitions() {
        var transitions = new ArrayList<XTransition>();

        initial.walk((Consumer<XTransition>) transitions::add);

        return transitions;
    }
}
