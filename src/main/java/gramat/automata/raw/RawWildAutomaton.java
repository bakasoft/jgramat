package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NState;
import gramat.automata.ndfa.SymbolWild;

import java.util.HashSet;
import java.util.LinkedList;

public class RawWildAutomaton extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context) {
        var state = context.initialAccepted();

        context.transitionWild(state, state);

        context.postBuildHook(() -> resolve_wild_state(state));
    }

    private void resolve_wild_state(NState root) {
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var trn : root.getTransitions()) {
            queue.add(trn.target);
        }

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = state.getTransitions();
                var hasWilds = transitions.stream().anyMatch(t -> t.symbol instanceof SymbolWild);
                if (!hasWilds && transitions.size() > 0) {
                    root.language.transition(state, root, null);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }
    }

}
