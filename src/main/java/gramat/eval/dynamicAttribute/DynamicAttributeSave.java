package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeSave extends SubAction {
    public DynamicAttributeSave(Action origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Save Dyn-Attribute";
    }
}
