package gramat.eval.staticAttribute;

import gramat.eval.TRXAction;
import gramat.eval.Evaluator;

public class StaticAttributeStart extends TRXAction {
    @Override
    public void run(Evaluator evaluator) {
        evaluator.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "BEGIN-ATTR";
    }
}
