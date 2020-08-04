package gramat.expressions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;
import gramat.engine.nodet.NTool;

public class TRX2 {

    public static void applyActions(NBuilder builder, NState initial, NState accepted, Action begin, Action commit, Action sustain) {
//        var beginTransitions = NTool.findOutgoingSymbolTransitions(initial);
        var beginTransitions = initial.getTransitions();
//        var commitTransitions = NTool.findIncomingSymbolTransitions(accepted);
        var commitTransitions = builder.root.findTransitionsByTarget(accepted);
//        var contentTransitions = NTool.findSymbolTransitions(initial, accepted);
        var contentTransitions = NTool.findAllTransitions(initial, accepted);

        for (var trn : beginTransitions) {
            trn.addAction(begin);
        }

        for (var trn : commitTransitions) {
            trn.addAction(commit);
        }

        for (var trn : contentTransitions) {
            if (!beginTransitions.contains(trn) && !commitTransitions.contains(trn)) {
                trn.addAction(sustain);
            }
        }
    }

}
