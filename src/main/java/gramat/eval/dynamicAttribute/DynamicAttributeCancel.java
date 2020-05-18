package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeCancel extends SubAction {
    public DynamicAttributeCancel(Action origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Cancel Dyn-Attribute";
    }
}
