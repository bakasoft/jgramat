package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeValueSave extends SubAction {
    public DynamicAttributeValueSave(Action origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Save Value Dyn-Attribute";
    }
}
