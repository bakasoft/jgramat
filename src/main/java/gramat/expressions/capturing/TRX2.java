package gramat.expressions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NTool;

public class TRX2 {

    public static void applyActions(NCompiler compiler, NState initial, NState accepted, Action begin, Action commit, Action sustain) {
        var beginTransitions = initial.getTransitions();
        var commitTransitions = compiler.lang.findTransitionsByTarget(accepted);
        var contentTransitions = NTool.findAllTransitions(initial, accepted);

        for (var trn : beginTransitions) {
            trn.actions.add(begin);
        }

        for (var trn : commitTransitions) {
            trn.actions.add(commit);
        }

        for (var trn : contentTransitions) {
            if (!beginTransitions.contains(trn) && !commitTransitions.contains(trn)) {
                trn.actions.add(sustain);
            }
        }
    }

}
