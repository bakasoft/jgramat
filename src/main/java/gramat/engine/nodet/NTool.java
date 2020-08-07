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
                    if (trn.symbol == null) {
                        queue.add(trn.target);
                    }
                    else {
                        result.add(trn);
                    }
                }
            }
        } while(queue.size() > 0);

        return result;
    }

    public static NTransitionList findIncomingSymbolTransitions(NState target) {
        var lang = target.lang;
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(target);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : lang.findTransitionsByTarget(state)) {
                    if (trn.symbol == null) {
                        queue.add(trn.source);
                    }
                    else {
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
                    if (trn.symbol != null) {
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

    public static NTransitionList computeInverseEmptyClosure(NLanguage lang, NTransition initial, NState stop) {
        var result = new NTransitionList();
        var queue = new LinkedList<NTransition>();

        queue.add(initial);

        while(queue.size() > 0) {
            var transition = queue.remove();

            if (result.add(transition) && transition.source != stop) {
                for (var trn : lang.findTransitionsByTarget(transition.source)) {
                    if (trn.symbol == null) {
                        queue.add(trn);
                    }
                }
            }
        }

        return result;
    }

    public static NTransitionList computeEmptyClosure(NLanguage lang, NTransition initial, NState stop) {
        var result = new NTransitionList();
        var queue = new LinkedList<NTransition>();

        queue.add(initial);

        while(queue.size() > 0) {
            var transition = queue.remove();

            if (result.add(transition) && transition.target != stop) {
                for (var trn : lang.findTransitionsBySource(transition.target)) {
                    if (trn.symbol == null) {
                        queue.add(trn);
                    }
                }
            }
        }

        return result;
    }
}
