package gramat.eval.staticAttribute;

import gramat.eval.TRXAction;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class StaticAttributeCancel extends SubAction {
    public StaticAttributeCancel(TRXAction origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.popAssembler();
    }

    @Override
    public String getDescription() {
        return "ROLLBACK-ATTR";
    }
}
