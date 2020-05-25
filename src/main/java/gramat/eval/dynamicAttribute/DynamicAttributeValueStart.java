package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class DynamicAttributeValueStart extends Action {
    @Override
    public void run(Evaluator evaluator) {
        evaluator.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "Start Value Dyn-Attribute";
    }
}
