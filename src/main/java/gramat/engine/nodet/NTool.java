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
        var root = target.root;
        var result = new NTransitionList();
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(target);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : root.findTransitionsByTarget(state)) {
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

}
