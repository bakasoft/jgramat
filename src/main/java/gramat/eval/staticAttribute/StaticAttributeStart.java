package gramat.eval.staticAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class StaticAttributeStart extends Action {
    @Override
    public void run(Evaluator evaluator) {
        evaluator.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "BEGIN-ATTR";
    }
}
