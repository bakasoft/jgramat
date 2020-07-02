package gramat.expressions;

import gramat.engine.SymbolWild;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Wildcard extends Expression {
    @Override
    public NState build(NBuilder builder, NState initial) {
        builder.newWildTransition(initial, initial);

        builder.maker.addTransitionHook(() -> {
            var queue = new LinkedList<NState>();
            var control = new HashSet<NState>();

            for (var trn : initial.getTransitions()) {
                queue.add(trn.target);
            }

            while(queue.size() > 0) {
                var state = queue.remove();

                if (control.add(state)) {
                    var transitions = state.getTransitions();
                    if (!transitions.hasWilds() && transitions.size() > 0) {
                        builder.newWildTransition(state, initial);

                        for (var trn : transitions) {
                            queue.add(trn.target);
                        }
                    }
                }
            }
        });

        return initial;
    }

    @Override
    public List<Expression> getChildren() {
        return List.of();
    }
}
