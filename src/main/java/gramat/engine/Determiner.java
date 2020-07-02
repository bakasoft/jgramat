package gramat.engine;

import gramat.engine.deter.DState;
import gramat.engine.nodet.*;

public class Determiner {

    public static DState compile(NMachine machine) {
        return new Determiner(machine).compile();
    }

    private final NRoot root;
    private final NState initial;
    private final NStateList accepted;

    private Determiner(NMachine machine) {
        this.initial = machine.initial;
        this.accepted = new NStateList(machine.accepted);
        this.root = machine.root;
    }

    private DState compile() {
//        cleanEmptyTransitions();
        return null;
    }

    private void cleanEmptyTransitions() {
        int movements;

        do {
            movements = 0;

            for (var transition : root.transitions) {
                if (transition.symbol instanceof SymbolEmpty) {
                    if (cleanEmptyTransition(transition)) {
                        movements++;
                    }
                }
            }
        } while(movements > 0);
    }

    private boolean cleanEmptyTransition(NTransition transition) {
        if (transition.badge != null) {
            for (var next : root.findTransitionsBySource(transition.target)) {
                if (next.badge == null) {
                    next.badge = transition.badge;
                }
                else {
                    var newTransition = root.newTransition(
                            next.source,
                            next.target,
                            next.symbol,
                            transition.badge);

                    newTransition.actions.addAll(next.actions);
                }
            }

            transition.badge = null;

            return true;
        }
        else if (transition.actions.size() > 0) {
            for (var next : root.findTransitionsBySource(transition.target)) {
                next.actions.addAll(transition.actions);
            }

            transition.actions.clear();
            return true;
        }
        else {
            return false;
        }
    }

}
