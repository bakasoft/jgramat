package gramat.automata.raw.units;

import gramat.automata.ndfa.*;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class RawWildAutomaton extends RawAutomaton {

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of();
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var wild = context.language.symbols.getWild();
        context.language.transition(initial, initial, wild);

        accepted.add(initial);

        context.postBuildHook(() -> resolve_wild_state(context.language, initial));
    }

    private void resolve_wild_state(NLanguage language, NStateSet roots) {
        var wild = language.symbols.getWild();
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var root : roots) {
            for (var trn : root.getTransitions()) {
                queue.add(trn.target);
            }
        }

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = state.getTransitions();
                var hasWilds = transitions.stream().anyMatch(s -> s.symbol.isWild());
                if (!hasWilds && transitions.size() > 0) {
                    language.transition(state, roots, wild);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }
    }

}
