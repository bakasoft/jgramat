package gramat.eval.object;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ObjectStart extends Action {


    @Override
    public String getDescription() {
        return "Object-Begin";
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.pushAssembler();
    }
}
