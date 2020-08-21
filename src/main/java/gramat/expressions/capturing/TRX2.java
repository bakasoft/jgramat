package gramat.expressions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

public class TRX2 {

    public static void applyActions(NCompiler compiler, NState initial, NState accepted, Action begin, Action commit) {
        var beginTransitions = initial.getTransitions();
        var commitTransitions = compiler.lang.findTransitionsByTarget(accepted);

        for (var trn : beginTransitions) {
            trn.actions.add(begin);
        }

        for (var trn : commitTransitions) {
            trn.actions.add(commit);
        }
    }

}
