package gramat.automata.raw.units;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;

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
    public State build(Builder builder, State initial) {
        builder.newWildTransition(initial, initial);

        builder.assembler.linkHook(initial, RawWildAutomaton::resolve_wild_state);

        return initial;
    }

    private static void resolve_wild_state(State root) {
        var language = root.language;
        var wild = language.symbols.getWild();
        var queue = new LinkedList<State>();
        var control = new HashSet<State>();

        for (var trn : root.transitions) {
            queue.add(trn.target);
        }

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                var transitions = state.transitions;
                var hasWilds = transitions.stream().anyMatch(s -> s.symbol.isWild());
                if (!hasWilds && transitions.size() > 0) {
                    language.newTransition(state, root, wild);

                    for (var trn : transitions) {
                        queue.add(trn.target);
                    }
                }
            }
        }
    }

}
