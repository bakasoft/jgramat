package gramat.engine.nodet;

import java.util.HashSet;
import java.util.LinkedList;

public class NTool {

    public static NTransitionList findOutgoingSymbolTransitions(NState initial) {
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(initial);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    if (trn.isSymbolNull()) {
                        queue.add(trn.target);
                    }
                    else {
                        if (!trn.isCheckNull()) {
                            throw new RuntimeException("expected null check");
                        }
                        result.add(trn);
                    }
                }
            }
        } while(queue.size() > 0);

        return result;
    }

    public static NTransitionList findIncomingSymbolTransitions(NState target) {
        var root = target.root;
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(target);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : root.findTransitionsByTarget(state)) {
                    if (trn.isSymbolNull()) {
                        queue.add(trn.source);
                    }
                    else {
                        if (!trn.isCheckNull()) {
                            throw new RuntimeException("expected null check");
                        }
                        result.add(trn);
                    }
                }
            }
        } while(queue.size() > 0);

        return result;
    }

    public static NTransitionList findSymbolTransitions(NState start, NState stop) {
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(start);

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    if (!trn.isSymbolNull()) {
                        result.add(trn);
                    }

                    if (trn.source != stop || trn.target == stop) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }

    public static NTransitionList findAllTransitions(NState start, NState stop) {
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(start);

        while(queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : state.getTransitions()) {
                    result.add(trn);

                    if (trn.source != stop || trn.target == stop) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }
}
