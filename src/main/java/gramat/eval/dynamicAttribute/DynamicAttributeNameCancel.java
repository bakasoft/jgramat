package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeNameCancel extends SubAction {
    public DynamicAttributeNameCancel(Action origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Cancel Name Dyn-Attribute";
    }
}
