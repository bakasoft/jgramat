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
    public NSegment build(NContext context) {
        var state = context.language.state();
        var wild = context.language.symbols.getWild();

        context.language.transition(state, state, wild);

        context.linkHook(state, RawWildAutomaton::resolve_wild_state);

        return context.segment(state, state);
    }

    private static void resolve_wild_state(NState root) {
        var language = root.language;
        var wild = language.symbols.getWild();
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        for (var trn : root.getTransitions()) {
            queue.add(trn.target);
        }

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = state.getTransitions();
                var hasWilds = transitions.stream().anyMatch(s -> s.symbol != null && s.symbol.isWild());
                if (!hasWilds && transitions.size() > 0) {
                    language.transition(state, root, wild);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }
    }

}
