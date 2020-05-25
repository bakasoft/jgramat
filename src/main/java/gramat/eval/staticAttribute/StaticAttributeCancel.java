package gramat.eval.staticAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class StaticAttributeCancel extends SubAction {
    public StaticAttributeCancel(Action origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.popAssembler();
    }

    @Override
    public String getDescription() {
        return null;
    }
}
