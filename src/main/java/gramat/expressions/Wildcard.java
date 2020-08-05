package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Wildcard extends Expression {
    @Override
    public NState build(NCompiler compiler, NState initial) {
        var wild = compiler.symbols.getWild();

        compiler.lang.newTransition(initial, initial, wild);

        compiler.addTransitionHook(() -> {
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
                        compiler.lang.newTransition(state, initial, wild);

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
