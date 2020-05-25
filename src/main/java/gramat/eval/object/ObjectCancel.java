package gramat.eval.object;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ObjectCancel extends Action {

    @Override
    public String getDescription() {
        return "Object-Rollback";
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.popAssembler();
    }
}
