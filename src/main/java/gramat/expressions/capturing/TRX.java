package gramat.expressions.capturing;

import gramat.engine.Action;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;

public class TRX {

    public static void applyActions(NBuilder builder, NState initial, NState accepted, Action begin, Action commit, Action rollback) {
        var group = builder.newGroup(begin, commit, rollback);
        var control = new HashSet<NState>();
        var queue = new LinkedList<NState>();

        queue.add(initial);

        do {
            var state = queue.remove();

            if (control.add(state)) {
                if (state == initial) {
                    state.marks.add(group.initialMark);
                }
                else if (state == accepted) {
                    state.marks.add(group.acceptedMark);
                }
                else {
                    state.marks.add(group.contentMark);
                }

                for (var transition : state.getTransitions()) {
                    queue.add(transition.target);
                }
            }
        } while(queue.size() > 0);
    }
}

